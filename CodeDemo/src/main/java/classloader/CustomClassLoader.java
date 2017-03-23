package classloader;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
/**
 * 
 * @Description热加载实现
 * @author liliangbing
 * @date 2017年3月23日
 *
 */
public class CustomClassLoader extends ClassLoader{
	String baseDir = "/Users/yourmall/Documents/workspace/Buss_Eclipse_work/tt/bin/";

    private String name;  
	public CustomClassLoader(ClassLoader parent,String className){
		super(parent);
		this.name = className;
	}
	
	/**
	 * 重写此方法，打破双亲委派机制。
	 */
	@Override
	protected Class<?> loadClass(String name, boolean resolve)
			throws ClassNotFoundException {
		if(this.name.equals(name)&&!"java".equals(name)){
            Class<?> c = findLoadedClass(name);
            if(c==null){
            	/**
            	 * 打破双亲委派机制。没有像父类ClassLoader一样调用c = parent.loadClass(name, false)
            	 * 而是直接调用重写findClass函数
            	 */
            	c = findClass(name);
            }
            /** 
             * 1、打破双亲委派机制。没有像父类ClassLoader一样调用c = parent.loadClass(name, false)，
             * 类的生命周期包括：加载、验证、准备、解析、初始化、使用、卸载。其中验证、准备、解析统称为连接 
             * 如果要连接类 
             */  
            if(resolve)
            	resolveClass(c);
		}
		return super.loadClass(name, resolve);
	}

	public Class<?> loadClass(){
		try {
			return loadClass(name,false);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		String filename = c2f(name);
		byte[] bytes = f2b(filename);
		return defineClass(name,bytes, 0, bytes.length);
	}
	public String c2f(String name){
		name = name.replace(".", File.separator);
		name= baseDir+name+".class";
		return name;
	}
	
	public byte[] f2b(String name) {
		RandomAccessFile file = null;
		FileChannel chanel = null;
		byte[] bytes = null;
		try{
			file = new RandomAccessFile(name, "r");
			chanel = file.getChannel();
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			bytes = new byte[(int)chanel.size()];
			int index = 0;
			while(chanel.read(buffer)>0){
				buffer.flip();
				while(buffer.hasRemaining()){
					bytes[index] = buffer.get();
					++index;
				}
				buffer.clear();
			}
		}catch (Exception e){
			
		}finally{
			if(chanel!=null){
				try{
					chanel.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			if(file!=null){
				try{
					file.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		return bytes;
	}
	
	
}
