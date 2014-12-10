/*******************************************************************************
 * Copyright (c) 2014 Rapicorp, Inc and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Rapicorp, Inc - initial API and implementation
 *******************************************************************************/
package infra;

import java.util.Arrays;
import java.util.List;


public class StringOutput extends MIOutput{
	private String[] strings;

	public StringOutput(String...strings) {
		this.strings = strings;
	}
	
	public List<String> serialize() {
		return Arrays.asList(strings);
	}
}
