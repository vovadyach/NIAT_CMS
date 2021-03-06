package com.niat.cms.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author gtament
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Пользователь не может изменить собственную роль")
public class UserChangedOwnRoleException extends RuntimeException {
}