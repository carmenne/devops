package com.carmenne;

import com.carmenne.web.i18n.I18nService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DevopsApplicationTests {

    @Autowired
    private I18nService i18nService;

    @Test
    public void testMessageByLocaleServices() throws Exception{
        String expectedResult = "Bootstrap starter template";
        String messageId = "index.main.callout";
        String actual = i18nService.getMessage(messageId);

        assertEquals("The actual and expected messages don't match",
                expectedResult, actual);
    }

}
