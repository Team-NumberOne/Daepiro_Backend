package com.numberone.daepiro.domain.user.service

import com.numberone.daepiro.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: UserRepository
) {
    // TODO: 회원 서비스 로직 세부 구현
}
