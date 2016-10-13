/*
 * Licensed to waterwave under one or more contributor
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package waterwave.common.service;

import java.util.Properties;

public class ShutdownService extends SingleThreadService {

	public SingleThreadService ts = null;
	
	

	public ShutdownService(SingleThreadService cs) {
		super();
		this.ts = cs;
		Runtime.getRuntime().addShutdownHook(this);
	}


	public void setCs(SingleThreadService ts) {
		this.ts = ts;
	}

	@Override
	public void onExit() {
		ts.onExit();
	}

	@Override
	public void run() {
		onExit();
	}

	@Override
	public void init(Properties pp) {

	}

	@Override
	public void onTime() {
		// TODO Auto-generated method stub
	}

}
