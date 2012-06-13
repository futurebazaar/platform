package com.fb.commons.ftp.to;

/**
 * @author nehaga
 *
 */
public class FTPDownloadTO{
	private String sourcePath;
	private String destinationPath;
	private String movePath = null;
	
	public String getSourcePath() {
		return sourcePath;
	}
	public void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}
	public String getDestinationPath() {
		return destinationPath;
	}
	public void setDestinationPath(String destinationPath) {
		this.destinationPath = destinationPath;
	}
	public String getMovePath() {
		return movePath;
	}
	public void setMovePath(String movePath) {
		this.movePath = movePath;
	}
	@Override
	public String toString(){
		return "Source Path : " + sourcePath +
				"\n Destination Path : " + destinationPath;
	}
	
}
