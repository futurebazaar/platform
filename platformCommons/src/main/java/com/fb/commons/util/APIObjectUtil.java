/**
 * 
 */
package com.fb.commons.util;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Utility class to copy data between Jaxb Generated web service layer classes and Request & response classes of Middle tier. Both ways.
 * 
 * @author vinayak
 * 
 */
public class APIObjectUtil {

	private static Log logger = LogFactory.getLog(APIObjectUtil.class);
	public static String DEFAULT_ENUM_MAPPING = "INTERNAL_ERROR";

	private boolean warnings = true;
	private ObjectConverter converter;

	/**
	 * Constructor to use when you dont want to log warnings.
	 */
	public APIObjectUtil(boolean warnings) {
		this.warnings = warnings;
		this.converter = new DefaultObjectConverter();
	}

	public APIObjectUtil() {
		this.converter = new DefaultObjectConverter();
	}

	/**
	 * @param convertor data conversion algorithm to use when copying
	 */
	public APIObjectUtil(ObjectConverter converter) {
		this.converter = converter;
	}

	/**
	 * Copies data from copyFrom object into copyTo object. The objects must confirm to the java bean method naming convention. 
	 * The Objects are not required to be of the same type. The copying will work if the both objects have equivalent method names. 
	 * This method loops through all the methods in the copyFrom Object that starts with "get" or "is" 
	 * and find equivalent "set" method in the copyTo Object. 
	 * If the getMethods return type is one of the primitive, Date, String, Number or Boolean 
	 * then it calls the setter on copyTo object with this data. 
	 * If the return type of the getter method is an array, then it loops through the contents of the
	 * array and builds the equivalent array for the setter method and sets the data in copyTo Object. 
	 * If the return type of the getter method is an Object, it calls the copyObject method recursively untill all the sub objects are copied over.
	 * 
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public void copyObject(Object copyFrom, Object copyTo) {
		if (copyFrom == null || copyTo == null) {
			// throw new Exception("Null objects passed.");
			return;
		}

		Class<?> cfClass = copyFrom.getClass();
		Class<?> ctClass = copyTo.getClass();

		Method[] methods = cfClass.getMethods();
		for (int i = 0; i < methods.length; i++) {

			if (methods[i].getName().startsWith("get") || methods[i].getName().startsWith("is")) {
				Method getMethod = methods[i];
				Class<?> getReturnType = getMethod.getReturnType();

				try {
					if (getReturnType.isArray()) {
						//needs to iterate through elements of array and do copy of all individual items.
						copyArray(copyFrom, cfClass, copyTo, ctClass, getMethod, getReturnType);

					} else if (isReturnTypePrimitive(getReturnType)) {
						//do the copy of values
						copyPrimitiveTypes(copyFrom, cfClass, copyTo, ctClass, getMethod, getReturnType);

					} else if (isReturnTypeEnum(getReturnType)) {
						copyEnum(copyFrom, cfClass, copyTo, ctClass, getMethod, getReturnType);

					} else {
						//this is an object which needs to be deep copied.
						copyChildObject(copyFrom, cfClass, copyTo, ctClass, getMethod, getReturnType);
					}
				} catch (Exception ex) {
					//catch and log one of IllegalAccessException, InstantiationException or InvocationTargetException then carry on
					if (warnings) {
						logger.warn("Exception while copying objects.", ex);
					}
					//potentially set a flag in minor error code of the root object? TODO
				}
			}
		}
	}

	private void copyChildObject(Object copyFrom, Class<?> cfClass, Object copyTo, Class<?> ctClass, Method getMethod, Class<?> getReturnType) 
			throws IllegalAccessException, InstantiationException, InvocationTargetException {

		Method setMethod = findSetMethod(copyFrom, cfClass, copyTo, ctClass, getMethod, getReturnType);
		Object setParameterObject = createSetParameterObject(ctClass, setMethod);
		//copy only if we can find equivalent set object.
		if (setParameterObject != null) {
			Object sourceData = converter.convert(getReturnType, getMethod.invoke(copyFrom, null));
			if (sourceData != null) {
				copyObject(sourceData, setParameterObject);
				setMethod.invoke(copyTo, new Object[] {setParameterObject});
			}
		}
	}

	private Object createSetParameterObject(Class<?> ctClass, Method setMethod)
			throws IllegalAccessException, InstantiationException {
		if (setMethod != null) {
			Class<?>[] parameterTypes = setMethod.getParameterTypes();
			//we expect only one parameter type for our set method.
			Object setParameterObject = parameterTypes[0].newInstance();
			//TODO for primatives this will throw a java.lang.InstantiationException?
			return setParameterObject;
		}
		return null;
	}

	private Method findSetMethod(Object copyFrom, Class<?> cfClass, Object copyTo, Class<?> ctClass, Method getMethod, Class<?> getReturnType) {
		String setMethodName = "set" + getMethod.getName().substring(3);
		Method[] setMethods = ctClass.getMethods();
		for (int i = 0; i < setMethods.length; i++) {
			if (setMethods[i].getName().equals(setMethodName)) {
				return setMethods[i];
			}
		}
		return null;
	}

	private void copyEnum(Object copyFrom, Class<?> cfClass, Object copyTo, Class<?> ctClass, Method getMethod, Class<?> getReturnType)
			throws IllegalAccessException, InstantiationException, InvocationTargetException {

		Method setMethod = findSetMethod(copyFrom, cfClass, copyTo, ctClass, getMethod, getReturnType);
		if (setMethod == null) {
			return;
		}
		Object getEnumObject = converter.convert(getReturnType, getMethod.invoke(copyFrom, null));
		if (getEnumObject == null) {
			//the enum is not populated in the incoming object.
			return;
		}
		Class<?>[] parameterTypes = setMethod.getParameterTypes();
		Object setEnumObject = this.createEnumObject(parameterTypes[0], getEnumObject);
		if (setEnumObject != null) {
			//set the new enum value
			setMethod.invoke(copyTo, new Object[] {setEnumObject});
		}
	}

	private Object createEnumObject(Class<?> setEnumClass, Object enumObject) throws IllegalAccessException, InstantiationException, InvocationTargetException {

		Method enumMethod = null;
		Object setEnumObject = null;
		String enumValue = enumObject.toString();

		try {
			enumMethod = setEnumClass.getMethod("fromValue", new Class[] {String.class});
			setEnumObject = enumMethod.invoke(null, new Object[] {enumValue});
		} catch (NoSuchMethodException e) {
			//this should not happen. unless we changed the way the classes are generated from jaxb.
			if (warnings) {
				logger.warn("The 'fromValue' method is not found in the enum class. Have we changed the way the classes are generated from jaxb? "
								+ " enumValue: " + enumValue + " setEnumClass: " + setEnumClass.toString(), e);
			}
			return null;
		} catch (InvocationTargetException e) {
			if (e.getCause() instanceof IllegalArgumentException) {
				logger.info("Enum Value '" + enumValue + "' not found in the class '" + setEnumClass.getName() + "'. " + e.getMessage());
				setEnumObject = enumMethod.invoke(null, new Object[] { APIObjectUtil.DEFAULT_ENUM_MAPPING });
			} else {
				throw e;
			}
		}
		return setEnumObject;
	}

	private boolean isReturnTypeEnum(Class<?> getReturnType) {
		if (Enum.class.isAssignableFrom(getReturnType)) {
			return true;
		}
		return false;
	}

	private void copyArray(Object copyFrom, Class<?> cfClass, Object copyTo, Class<?> ctClass, Method getMethod, Class<?> getReturnType)
			throws IllegalAccessException, InstantiationException, InvocationTargetException {

		Method setMethod = findSetMethod(copyFrom, cfClass, copyTo, ctClass, getMethod, getReturnType);
		if (setMethod != null) {
			Class<?>[] parameterTypes = setMethod.getParameterTypes();
			//we expect only one parameter type for our set method.
			if (parameterTypes[0].isArray()) {
				Class<?> setDataClass = parameterTypes[0].getComponentType();
				if (isReturnTypePrimitive(setDataClass)) {
					//this is an array of primitive types.
					Object sourceData = converter.convert(getReturnType, getMethod.invoke(copyFrom, null));
					setMethod.invoke(copyTo, new Object[] {sourceData});
				} else if (isReturnTypeEnum(setDataClass)) {
					//the sourceData is an array of Enums.
					Object[] sourceData = converter.convert(getReturnType, (Object[]) getMethod.invoke(copyFrom, null));
					if (sourceData != null) {
						Object setObjectsArray = Array.newInstance(setDataClass, sourceData.length);
						if (sourceData.length > 0) {
							for (int i = 0; i < sourceData.length; i++) {
								if (sourceData[i] != null) {
									Object setData = createEnumObject(setDataClass, sourceData[i]);
									if (setData != null) {
										Array.set(setObjectsArray, i, setData);
									}
								}
							}
						}
						//now set this newly created array into our copyTo object.
						setMethod.invoke(copyTo, new Object[] {setObjectsArray});
					}
				} else {
					//the sourceData object is an array.
					Object[] sourceData = converter.convert(getReturnType, (Object[]) getMethod.invoke(copyFrom, null));

					if (sourceData != null) {
						Object setObjectsArray = Array.newInstance(setDataClass, sourceData.length);
						if (sourceData.length > 0) {
							for (int i = 0; i < sourceData.length; i++) {
								Object data = converter.convert(setDataClass, sourceData[i]);
								if (data != null) {
									Object setData = setDataClass.newInstance();
									copyObject(data, setData);
									Array.set(setObjectsArray, i, setData);
								}
							}
						}
						//now set this newly created array into our copyTo object.
						setMethod.invoke(copyTo, new Object[] {setObjectsArray});
					}
				}
			}
		} else {
			//logger.warn("No such method "); //TODO ?
		}
	}

	private void copyPrimitiveTypes(Object copyFrom, Class<?> cfClass, Object copyTo, Class<?> ctClass, Method getMethod, Class<?> getReturnType) throws IllegalAccessException, InvocationTargetException {

		try {
			Class<?>[] setParameters = new Class[] {getReturnType};
			//get the equivalent set method. The getMethod name either begins with "get" or "is".
			Method setMethod = null;
			if (getMethod.getName().startsWith("get")) {
				setMethod = ctClass.getMethod("set" + getMethod.getName().substring(3), setParameters);
			} else {
				setMethod = ctClass.getMethod("set" + getMethod.getName().substring(2), setParameters);
			}

			Object sourceData = converter.convert(getReturnType, getMethod.invoke(copyFrom, null));
			setMethod.invoke(copyTo, new Object[] { sourceData });

		} catch (NoSuchMethodException e) {
			//ok. the copyTo object doesn't have the equivalent set method. ignore the exception and move on.
			logger.debug("The copyTo object doesn't have the equivalent set method. " + e.getMessage());
		}
	}

	private boolean isReturnTypePrimitive(Class<?> classInstance) {
		if (classInstance.isAssignableFrom(Date.class)
				|| Date.class.isAssignableFrom(classInstance)
				|| classInstance.isAssignableFrom(String.class)
				|| classInstance.isAssignableFrom(Number.class)
				|| Number.class.isAssignableFrom(classInstance)
				|| classInstance.isAssignableFrom(Boolean.class)
				|| classInstance.isPrimitive()) {
			return true;
		}
		return false;
	}
}
