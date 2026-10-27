package project.karbonzero.Controllers.Channel;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import project.karbonzero.Models.DTO.Mail.ChannelDTO;

@RestController
@RequestMapping("/apiChannel")
public class ChannelController {
    @Value("${EXTERNAL_ADMIN_DOMAIN:}")
    private String externalAdminDomain;
    @Value("${EXTERNAL_API_KEY}")
    private String externalApiKey;


    @GetMapping("/getChannel")
    private ResponseEntity<?> getChannel (HttpServletRequest request) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if ("channelToken".equals(cookie.getName())) {
                headers.set("Cookie", "channelToken=" + cookie.getValue());
                break;
            }
        }
        HttpEntity<String> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                externalAdminDomain + "/apiCompany/getChannelByApiKey/" + externalApiKey,
                HttpMethod.GET,
                entity,
                String.class
        );
    }

    @PutMapping("/updateChannel")
    private ResponseEntity<?> updateChannel (@RequestBody ChannelDTO dto, HttpServletRequest request) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if ("channelToken".equals(cookie.getName())) {
                headers.set("Cookie", "channelToken=" + cookie.getValue());
                break;
            }
        }
        HttpEntity<ChannelDTO> entity = new HttpEntity<>(dto, headers);

        return restTemplate.exchange(
                externalAdminDomain + "/apiCompany/updateChannelByApiKey/" + externalApiKey,
                HttpMethod.PUT,
                entity,
                String.class
        );
    }

    @GetMapping("/getChannelPostApproved")
    private ResponseEntity<?> getChannelPostApproved (HttpServletRequest request) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if ("channelToken".equals(cookie.getName())) {
                headers.set("Cookie", "channelToken=" + cookie.getValue());
                break;
            }
        }
        HttpEntity<String> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                externalAdminDomain + "/apiCompany/getChannelPostApproved/" + externalApiKey,
                HttpMethod.GET,
                entity,
                String.class
        );
    }

    @GetMapping("/getChannelPostNoApproved")
    private ResponseEntity<?> getChannelPostNoApproved (HttpServletRequest request) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if ("channelToken".equals(cookie.getName())) {
                headers.set("Cookie", "channelToken=" + cookie.getValue());
                break;
            }
        }

        HttpEntity<String> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                externalAdminDomain + "/apiCompany/getChannelPostNoApproved/" + externalApiKey,
                HttpMethod.GET,
                entity,
                String.class
        );
    }

    @PutMapping("/approvePost/{id}")
    private ResponseEntity<?> approvePost (@PathVariable String id, HttpServletRequest request) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if ("channelToken".equals(cookie.getName())) {
                headers.set("Cookie", "channelToken=" + cookie.getValue());
                break;
            }
        }

        HttpEntity<String> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                externalAdminDomain + "/apiCompany/approvePost/" + id,
                HttpMethod.PUT,
                entity,
                String.class
        );
    }

    @DeleteMapping("/deletePost/{id}")
    private ResponseEntity<?> deletePost (@PathVariable String id, HttpServletRequest request) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if ("channelToken".equals(cookie.getName())) {
                headers.set("Cookie", "channelToken=" + cookie.getValue());
                break;
            }
        }

        HttpEntity<String> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                externalAdminDomain + "/apiCompany/deleteChannelPost/" + id,
                HttpMethod.DELETE,
                entity,
                String.class
        );
    }
}
