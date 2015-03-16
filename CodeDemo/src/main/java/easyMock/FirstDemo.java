package easyMock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.LinkedList;
import org.junit.Test;

/** 
 * @ClassName: FirstDemo 
 * @Description:  
 * @date 2014-12-25 
 */
public class FirstDemo {
	
	@Test
	public void t1(){
		LinkedList mockedList = mock(LinkedList.class);   
		System.out.println(mockedList.get( 999 ));  
	}
	
	@Test
	public void t2(){
		LinkedList mockedList = mock(LinkedList.class);  
		// 模拟获取第一个元素时，返回字符串first
		when(mockedList.get(0)).thenReturn("first");
		// 此时打印输出first
		System.out.println(mockedList.get(0));
	}
	@Test
	public void t3(){
		LinkedList mockedList = mock(LinkedList.class);  
		// 模拟获取第二个元素时，抛出RuntimeException   
		when(mockedList.get( 1 )).thenThrow( new  RuntimeException());   
		// 此时将会抛出RuntimeException   
		System.out.println(mockedList.get( 1 ));  
	}
	@Test
	public void t4(){
		LinkedList mockedList = mock(LinkedList.class);  
	/*	// anyInt()匹配任何int参数，这意味着参数为任意值，其返回值均是element  
		when(mockedList.get(Any())).thenReturn("element");  
		  
		// 此时打印是element  
		System.out.println(mockedList.get(999));  */
	}
}
