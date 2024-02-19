package com.application.backend.helper;

import com.application.backend.exception.NoPermissionException;
import com.application.backend.userdetails.CustomUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

public class CheckRole {
    public static void isCreate(){
        CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.getRole().isCREATE()) throw new NoPermissionException();
    }
    public static void isRead(){
        CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.getRole().isREAD()) throw new NoPermissionException();
    }

    public static void isUpdate(){
        CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.getRole().isUPDATE()) throw new NoPermissionException();
    }

    public static void isDelete(){
        CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.getRole().isDELETE()) throw new NoPermissionException();

    }
}
