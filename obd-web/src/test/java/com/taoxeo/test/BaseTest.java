/*
 * Copyright (C) 2018 TaoXeo. All rights reserved.
 */
package com.taoxeo.test;


import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.wkhmedical.TApplication;


/**
 * The Class BaseTest.
 *
 * @author Derek
 * @since 1.0, 2018-9-13
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.NONE, classes = TApplication.class)
public class BaseTest {
}
