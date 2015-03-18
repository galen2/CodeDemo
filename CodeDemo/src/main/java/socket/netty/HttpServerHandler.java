package socket.netty;
/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpHeaders.Values;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;
import io.netty.util.CharsetUtil;



public class HttpServerHandler extends ChannelHandlerAdapter {

	private HttpRequest request;
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		
		if (msg instanceof HttpRequest) {
            request = (HttpRequest) msg;
            DecoderResult decoderResult = request.getDecoderResult();
            String uri = request.getUri();
            System.out.println("Uri:" + uri);
        }
		
		 if (request.getMethod() .equals("post")) { // 处理POST请求  
	            HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(  
	                    new DefaultHttpDataFactory(false), req);  
	            InterfaceHttpData postData = decoder.getBodyHttpData("q"); // //  
	                                                                        // 读取从客户端传过来的参数  
	            String question = "";  
	            if (postData.getHttpDataType() == HttpDataType.Attribute) {  
	                Attribute attribute = (Attribute) postData;  
	                question = attribute.getValue();  
	                System.out.println("q:" + question);  
	  
	            }  
	            if (question != null && !question.equals("")) {  
	  
	                HttpResponse res = new DefaultHttpResponse(HTTP_1_1, OK);  
	                String data = "<html><body>你好，POST</body><html>";  
	                ChannelBuffer content = ChannelBuffers.copiedBuffer(data,  
	                        CharsetUtil.UTF_8);  
	                res.setHeader(CONTENT_TYPE, "text/html; charset=UTF-8");  
	                res.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, "*");  
	                setContentLength(res, content.readableBytes());  
	                res.setContent(content);  
	                sendHttpResponse(ctx, req, res);  
	  
	            }  
	            
	            
        if (msg instanceof HttpContent) {
            HttpContent content = (HttpContent) msg;
            ByteBuf buf = content.content();
            System.out.println(buf.toString(io.netty.util.CharsetUtil.UTF_8));
            buf.release();

            String res = "I am OK";
            FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1,
                    OK, Unpooled.wrappedBuffer(res.getBytes("UTF-8")));
            response.headers().set(CONTENT_TYPE, "text/plain");
            response.headers().set(CONTENT_LENGTH,
                    response.content().readableBytes());
            if (HttpHeaders.isKeepAlive(request)) {
                response.headers().set(CONNECTION, Values.KEEP_ALIVE);
            }
            ctx.write(response);
            ctx.flush();
        }
	        
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		ctx.close();
	}
}
