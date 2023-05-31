package com.hyunn.carrot.test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@Controller
public class KakaopayController {

    @GetMapping("/kakaopay")
    public void kakaopay(HttpServletResponse response) {
        try {
            // 카카오페이 결제 준비 요청을 보냄
            URL address = new URL("https://kapi.kakao.com/v1/payment/ready");
            HttpURLConnection connection = (HttpURLConnection) address.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "KakaoAK 27809591c7e74888a348f6c8ecef80da");
            connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
            connection.setDoOutput(true);

            // 결제 준비 요청에 필요한 파라미터 설정
            String parameter = "cid=TC0ONETIME"
                    + "&partner_order_id=partner_order_id"
                    + "&partner_user_id=partner_user_id"
                    + "&item_name=Kandy"
                    + "&quantity=1"
                    + "&total_amount=10000000"
                    + "&vat_amount=200"
                    + "&tax_free_amount=0"
                    + "&approval_url=http://localhost:9000/"
                    + "&fail_url=http://localhost:9000/"
                    + "&cancel_url=http://localhost:9000/";

            // 파라미터 전송
            OutputStream send = connection.getOutputStream();
            DataOutputStream dataSend = new DataOutputStream(send);
            dataSend.writeBytes(parameter);
            dataSend.close();

            // 응답 처리
            int result = connection.getResponseCode();
            InputStream receive;

            if (result == 200) {
                receive = connection.getInputStream();
            } else {
                receive = connection.getErrorStream();
            }

            InputStreamReader read = new InputStreamReader(receive);
            BufferedReader change = new BufferedReader(read);
            StringBuilder responseBuilder = new StringBuilder();

            String line;
            while ((line = change.readLine()) != null) {
                responseBuilder.append(line);
            }

            // 응답 데이터 파싱
            String jsonResponse = responseBuilder.toString();
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);
            String nextRedirectPcUrl = jsonObject.get("next_redirect_pc_url").getAsString();

            // 다음 단계로 리다이렉트
            response.sendRedirect(nextRedirectPcUrl);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
