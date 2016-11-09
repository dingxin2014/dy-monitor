package com.dooioo.bank.controller;

import com.dooioo.base.AppBaseController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@RequestMapping(value = "/bank")

public class BankBillController extends AppBaseController {

    private static final Log log = LogFactory.getLog(BankBillController.class);


}
