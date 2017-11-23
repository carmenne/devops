package com.carmenne.web.controllers;

import com.carmenne.backend.service.EmailService;
import com.carmenne.web.domain.frontend.FeedbackPojo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ContactController {

    /**
     * The application logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(ContactController.class);


    /** The key which identifies the feedback pojo in model*/
    private static final String FEEDBACK_MODEL_KEY = "feedback";

    /**The Contact Us view */
    private static final String CONTACT_US_VIEW_NAME = "contact/contact";

    @Autowired
    private EmailService emailService;

    @RequestMapping(value = "/contact", method = RequestMethod.GET)
    public String contactGet(ModelMap model) {
        FeedbackPojo feedbackPojo = new FeedbackPojo();
        model.addAttribute(FEEDBACK_MODEL_KEY, feedbackPojo);
        return CONTACT_US_VIEW_NAME;

    }

    @RequestMapping(value = "/contact", method = RequestMethod.POST)
    public String contactPost(@ModelAttribute(FEEDBACK_MODEL_KEY) FeedbackPojo feedbackPojo) {

        LOG.debug("Feedback content: {}", feedbackPojo);
        emailService.sendFeedbackEmail(feedbackPojo);
        return CONTACT_US_VIEW_NAME;

    }
}
