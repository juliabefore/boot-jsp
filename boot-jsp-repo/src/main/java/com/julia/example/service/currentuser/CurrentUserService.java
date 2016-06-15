package com.julia.example.service.currentuser;

import com.julia.example.domain.CurrentUser;

public interface CurrentUserService {

    boolean canAccessUser(CurrentUser currentUser, Long userId);

}
