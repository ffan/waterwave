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
package waterwave.proxy.bio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import shuisea.common.buffer.BufferPool;
import shuisea.common.service.ShutdownService;
import shuisea.common.service.SingleThreadService;
import shuisea.common.util.PropertiesUtil;
import waterwave.net.bio.BioClient;
import waterwave.net.bio.BioServer;
import waterwave.proxy.router.ProxyRouter;

public class ProxyBioServerService extends SingleThreadService {

	@Override
	public void run() {
		for (;;) {
			try {
				Thread.sleep(1000);
				onTime();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		ProxyBioServerService service = new ProxyBioServerService();
		Properties pp = new Properties();

		int remortPort = 80;
		remortPort = 9200;
		remortPort = 3306;

		pp.put("remortPort", remortPort);

		service.init(pp);

	}

	@Override
	public void init(Properties p) {
		//
		log.log(9, "init...");
		new ShutdownService(this);

		//
		int poolSize = Runtime.getRuntime().availableProcessors() * 10;

		

		ProxyBioDataDealerFactory bioDataDealerFactory = new ProxyBioDataDealerFactory();

		//
		int bpSize = 400;
		//
		int bpBufferSize = 2 * 1024 * 1024;

		//
		int serverPort = 8001;

		PropertiesUtil pp = new PropertiesUtil(p);
		serverPort = pp.getInt("serverPort", serverPort);
		poolSize = pp.getInt("poolSize", poolSize);
		
		ExecutorService serverES = Executors.newFixedThreadPool(poolSize, Executors.defaultThreadFactory());
		ExecutorService clientES = Executors.newFixedThreadPool(poolSize, Executors.defaultThreadFactory());

		BufferPool bp1 = new BufferPool(bpSize, bpBufferSize);
		BufferPool bp2 = new BufferPool(bpSize, bpBufferSize);
		BioServer bioServer = null;
		BioClient bioClient = new BioClient(clientES, bp2);
		try {
			bioServer = new BioServer(serverPort, serverES, bp1, bioDataDealerFactory);
		} catch (IOException e) {
			e.printStackTrace();
		}

		bioDataDealerFactory.setClient(bioClient);
		bioDataDealerFactory.setServer(bioServer);

		// IP
		String ipStr = null;
		int remortPort = 80;

		remortPort = 9300;
		remortPort = 9200;
		remortPort = 11200;
		remortPort = 3306;

		ipStr = "www.baidu.com";
		ipStr = "news.163.com";
		ipStr = "www.bing.com";
		ipStr = "10.213.33.176";
		ipStr = "127.0.0.1";
		
		remortPort = pp.getInt("remortPort", remortPort);
		ipStr = pp.getString("ipStr", ipStr);
		

		InetAddress ip = null;
		
		try {
			ip = InetAddress.getByName(ipStr);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		ProxyRouter.staticRemoteIp = ip;
		ProxyRouter.staticRemotePort = remortPort;

		bioServer.start();

		log.log(9, "init finished");

	}

	@Override
	public void onExit() {
		log.log(9, "exit...");
		// TODO Auto-generated method stub

	}

	@Override
	public void onTime() {
		// TODO Auto-generated method stub

	}

}
