package com.management.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.json.JSONObject;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("${endpoints}/payments")
public class PaymentGatewayController {

	@Autowired
	private Environment env;

	@Operation(summary = "Payment Details", description = "This API used for get Payment Details.")
	@Tag(name = "Payment Gateway", description = "Note: A proper frontend implementation is required to access this Payments API's; otherwise, it may not function correctly.")
	@GetMapping("/paymentDetails")
	@ResponseBody
	public String paymentDetails() {
		String rzpKeyId = env.getProperty("rzp_key_id");
		String rzpCurrency = env.getProperty("rzp_currency");
		String rzpCompanyName = env.getProperty("rzp_company_name");
		String paymentDetailsJson = "{" + "\"rzp_key_id\": \"" + rzpKeyId + "\"," + "\"rzp_currency\": \"" + rzpCurrency
				+ "\"," + "\"rzp_company_name\": \"" + rzpCompanyName + "\"" + "}";

		return paymentDetailsJson;
	}

	@Operation(summary = "Created Payment", description = "This API used for create new payment order.")
	@Tag(name = "Payment Gateway", description = "Note: A proper frontend implementation is required to access this Payments API's; otherwise, it may not function correctly.")
	@GetMapping("/createPayment/{amount}")
	@ResponseBody
	public String createPaymentOrderId(@PathVariable String amount) {
		String orderId = null;
		try {
			RazorpayClient razorpay = new RazorpayClient(env.getProperty("rzp_key_id"),
					env.getProperty("rzp_key_secret"));
			JSONObject orderRequest = new JSONObject();
			orderRequest.put("amount", amount); // amount in the smallest currency unit
			orderRequest.put("currency", env.getProperty("rzp_currency"));
			orderRequest.put("receipt", "order_rcptid_11");
			Order order = razorpay.orders.create(orderRequest);
			orderId = order.get("id");
		} catch (RazorpayException e) {
			System.out.println(e.getMessage());
		}
		return orderId;
	}

	@Operation(summary = "Payment Response", description = "This API used for get Payment response.")
	@Tag(name = "Payment Gateway", description = "Note: A proper frontend implementation is required to access this Payments API's; otherwise, it may not function correctly.")
	@RequestMapping(value = {
			"/paymentExecution/{amount}/{contactNumber}/{companyName}/{currency}/{description}" }, method = RequestMethod.POST)
	public ResponseEntity<Object> paymentSuccess(@RequestParam("razorpay_payment_id") String razorpayPaymentId,
			@RequestParam("razorpay_order_id") String razorpayOrderId,
			@RequestParam("razorpay_signature") String razorpaySignature, @PathVariable Float amount,
			@PathVariable Integer contactNumber, @PathVariable String companyName, @PathVariable String currency,
			@PathVariable String description, RedirectAttributes redirectAttributes) {
		JSONObject responseJson = new JSONObject();

		responseJson.put("status", "success");
		responseJson.put("message", "Payment successful!");
		responseJson.put("payment_id", razorpayPaymentId);
		responseJson.put("order_id", razorpayOrderId);
		responseJson.put("signature", razorpaySignature);

		return ResponseEntity.ok(responseJson.toString());
	}
}