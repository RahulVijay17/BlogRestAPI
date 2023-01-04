package com.myb.blogrestapi.service;

import com.myb.blogrestapi.dto.LoginDto;
import com.myb.blogrestapi.dto.RegisterDto;

public interface AuthService {

    String login(LoginDto loginDto);

    String register(RegisterDto registerDto);
}
