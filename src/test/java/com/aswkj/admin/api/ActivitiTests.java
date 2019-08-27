package com.aswkj.admin.api;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivitiTests {

  @Autowired
  RepositoryService repositoryService;


  @Autowired
  RuntimeService runtimeService;


//  @Test
//  public void testStart() {
//    ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my_test_proccess");
//    System.out.println(processInstance);
//  }


}
