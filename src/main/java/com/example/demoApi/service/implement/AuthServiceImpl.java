package com.example.demoApi.service.implement;

import com.example.demoApi.dto.request.IntrospectRequest;
import com.example.demoApi.dto.request.LoginRequestDTO;
import com.example.demoApi.dto.response.IntrospectResponse;
import com.example.demoApi.dto.response.LoginResponseDTO;
import com.example.demoApi.entity.User;
import com.example.demoApi.exception.AppException;
import com.example.demoApi.exception.ErrorCode;
import com.example.demoApi.repository.UserRepository;
import com.example.demoApi.service.AuthService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimNames;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value; // ĐÚNG
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository  userRepository;
    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
            var token = request.getToken();
            JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
            SignedJWT jwt = SignedJWT.parse(token);

            Date expiryTime = jwt.getJWTClaimsSet().getExpirationTime();

            var isVerify = jwt.verify(verifier); //true or false

            return IntrospectResponse.builder()
                    .valid( isVerify && expiryTime.after(new Date()))
                    .build();
    }
    public LoginResponseDTO login(LoginRequestDTO request)
    {
        var user = userRepository.findByName(request.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authenticated) {
            throw new AppException(ErrorCode.LOGIN_FAILD);
        }
        var token = generateToken(user);
        return LoginResponseDTO.builder()
                .token(token)
                .build();
    }
    @Override
    public String buildScope (User user){
        StringJoiner stringJoiner = new StringJoiner(" ");
        if(!user.getRoles().isEmpty()){
            user.getRoles().forEach(stringJoiner::add);
        }
        return stringJoiner.toString();
    }
    //token = header + payload + signature
    @Override
    public String generateToken(User user)
    {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        JWTClaimsSet jwtClaimsSet =  new JWTClaimsSet.Builder()
                .subject(user.getName())
                .issuer("demoApi.com") //định danh thường là domain
                .issueTime(new Date()) //
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli())) //hết hạn
                .claim("scope", buildScope(user)) //custom
                .build();

//        Payload payload = new Payload(jwtClaimsSet.toJSONObject()); //nội dung gửi
//        JWSObject jwsObject = new JWSObject(header,payload);
        SignedJWT signedJWT = new SignedJWT(header, jwtClaimsSet);

        //ký token
        try {
            // 4. Ký Token (Sử dụng StandardCharsets để đồng bộ giữa các OS)
            JWSSigner signer = new MACSigner(SIGNER_KEY.getBytes(StandardCharsets.UTF_8));
            signedJWT.sign(signer);
            // 5. Trả về chuỗi JWT hoàn chỉnh
            return signedJWT.serialize();
        } catch (JOSEException ex) {
            log.error("Lỗi khi ký Token: ", ex);
            throw new RuntimeException("Không thể tạo Token", ex);
        }
    }
}
