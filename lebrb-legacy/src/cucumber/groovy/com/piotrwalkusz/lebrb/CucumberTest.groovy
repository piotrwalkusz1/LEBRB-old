package com.piotrwalkusz.lebrb

import cucumber.api.CucumberOptions
import cucumber.api.junit.Cucumber
import org.junit.runner.RunWith

@RunWith(Cucumber)
@CucumberOptions(features = 'src/cucumber/resources/features/', plugin = 'pretty',
                 glue = 'com.piotrwalkusz.lebrb.steps', tags = '~@ignore')
class CucumberTest {

    void cucumberTest() {}
}