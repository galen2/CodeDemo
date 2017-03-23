package classloader;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @Description
 * 1、热部署功能测试；
 * 2、在其他地方编写相应class文件实现Printer接口；
 * 3、然后启动服务，不断调试此文件输出内容，验证在服务启动状态下，如果通过重新加载class文件来实现
 * 内容修改之后立即被加载使用，无需重启。即热部署
 * @author liliangbing
 * @date 2017年3月23日
 *
 */
public class Client {
	public static void main(String[] args) {
		
		String name = "com.Printer";
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			try {
				while(true){
					System.out.println("请修改加载类Printer的输出内容，然后点击继续");
					String line = reader.readLine();
					if(line !=null&&line.length()>1){
						/**
						 * 这里是实现热部署的关键，每次都重建一个类加载器，这样才保证同一份class文件被再次加载
						 */
						CustomClassLoader loader = new CustomClassLoader(Client.class.getClassLoader(), name);
						Class<?> c = loader.loadClass();
						IPrinter printer = (IPrinter)c.newInstance();
						printer.print();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
}
