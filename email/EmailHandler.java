package com.application.backend.email;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import com.application.backend.helper.Helper;
import com.application.backend.model.Order;
import com.application.backend.model.OrderDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailHandler {
	
	@Autowired
	private JavaMailSender javaMailSender;
	

    private final String userSend = System.getenv("EMAIL_SEND_CLIENT");

	
	// send normal email
	public void sendEmail(String sendTo, 
			String subject, String content,
			MultipartFile[] files) throws MessagingException {

		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
					
		String mailContent = "<p>" + content +"</p>";

		// set value
		helper.setFrom(userSend);
		helper.setTo(sendTo);
		helper.setSubject(subject);
		helper.setText(mailContent, true);
		
		
		// set attachment
		List<String> fileNames = new ArrayList<>();
		List<InputStreamSource> sources = new ArrayList<>();
		
		if(files != null) {
			
			Arrays.asList(files).stream().forEach(file -> {
				 String fileName = StringUtils.cleanPath(file.getOriginalFilename());
				 
				 InputStreamSource source = new InputStreamSource() {

						@Override
						public InputStream getInputStream() throws IOException {
							return file.getInputStream();
							}
						};
						
				sources.add(source);
				fileNames.add(fileName);
            });
		
			
			for(int i = 0; i < fileNames.size(); ++i) {
				helper.addAttachment(fileNames.get(i), sources.get(i));
			}
		               
		
		}
			
		javaMailSender.send(message);
	}
	
	// send verify email
	public void sendVerifyEmail(String sendTo ,String verifyCode ) throws MessagingException {

		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		
		String mailContent = "<p> Mã xác thực của bạn là: "+verifyCode+"</p>";

		// set value
		helper.setFrom(userSend);
		helper.setTo(sendTo);
		helper.setSubject("Mã xác thực email");
		helper.setText(mailContent, true);

		javaMailSender.send(message);
	}

	// send active email
	public void sendActiveEmail(String email) throws MessagingException {

		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		String mailContent = "<a href=\""+System.getenv("API_CLIENT")
				+"/auth/activeAccount?email="+email+"\">Click here to active your account</a>";

		// set value
		helper.setFrom(userSend);
		helper.setTo(email);
		helper.setSubject("Kích hoạt tài khoản");
		helper.setText(mailContent, true);

		javaMailSender.send(message);
	}

	// send order email
	public void sendOrderEmail(List<OrderDetail> orderDetails, Order order) throws MessagingException{
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		StringBuilder string = new StringBuilder();

		string.append("<p> Id Order: "+ order.getId()  +"</p>");
		string.append("<p> Order at: "+ Helper.getDate(new Date())  +"</p>");
		string.append("<p> Address: "+ order.getAddress() +"</p>");
		string.append("<p>Total: "+ Helper.convertPrice(order.getTotal()) +" (shipping fee included)</p>");
		string.append("<table width='100%' border='1' align='center'>");
		string.append("<thead>" +
				"<tr><th>ID ITEM</th>" +
				"<th>IMAGE</th>" +
				"<th>NAME</th>" +
				"<th>COLOR</th>" +
				"<th>SIZE</th>" +
				"<th>GENDER</th>" +
				"<th>QUANTITY</th>" +
				"<th>PRICE</th>" +
				"</tr></thead>");
		string.append("<tbody>");

		for(OrderDetail orderDetail: orderDetails){
			string.append("<tr><th>"+orderDetail.getId()+"</th>" +
					"<th><img style='width: 100px; height: 100px;' src='"+orderDetail.getProductItem().getPrimaryImage()+"' /></th>" +
					"<th>"+orderDetail.getProductItem().getProduct().getName()+"</th>" +
					"<th>"+orderDetail.getProductItem().getColor().getName()+"</th>" +
					"<th>"+orderDetail.getProductItem().getSize()+"</th>" +
					"<th>"+orderDetail.getProductItem().getProduct().getGender()+"</th>" +
					"<th>"+orderDetail.getQuantity()+"</th>" +
					"<th>"+Helper.convertPrice(orderDetail.getProductItem().getPrice())+"</th>" +
					"</tr>");
		}

		string.append("</tbody>");
		string.append("</table>");

		// set value
		helper.setFrom(userSend);
		helper.setTo(order.getEmail());
		helper.setSubject("Xác nhận mua hàng");
		helper.setText(string.toString(), true);

		javaMailSender.send(message);
	}

}
