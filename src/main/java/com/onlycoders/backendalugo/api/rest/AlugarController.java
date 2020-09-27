package com.onlycoders.backendalugo.api.rest;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "Aluguel")
@RequestMapping("/aluguel")
@CrossOrigin("*")
public class AlugarController {

}
