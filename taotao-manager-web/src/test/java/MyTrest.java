import org.junit.Test;

import com.taotao.web.util.FastDFSClient;

public class MyTrest {
	@Test
	public void testFastDfsClient() throws Exception {
		//必须绝对路径
	    FastDFSClient fastDFSClient = new FastDFSClient("F:/mars/taotao-manager-web/src/main/resources/resource/fastdfs.conf");
	    String string = fastDFSClient.uploadFile("C:/Users/ligui/Pictures/u=905360048,3305376459&fm=26&gp=0.jpg");
	    System.out.println(string);
	    
	}
}
