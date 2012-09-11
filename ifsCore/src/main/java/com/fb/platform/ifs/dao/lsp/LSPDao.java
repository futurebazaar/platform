/**
 * 
 */
package com.fb.platform.ifs.dao.lsp;

import java.util.List;

import com.fb.platform.ifs.to.lsp.LSP;

/**
 * @author vinayak
 *
 */
public interface LSPDao {

	public List<LSP> loadAll();
}
