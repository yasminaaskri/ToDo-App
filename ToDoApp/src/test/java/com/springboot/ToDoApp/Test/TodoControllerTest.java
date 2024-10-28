package com.springboot.ToDoApp.Test;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.time.LocalDate;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.springboot.ToDoApp.todo.Todo;
import com.springboot.ToDoApp.todo.TodoController;
import com.springboot.ToDoApp.todo.TodoRepository;

@WebMvcTest(TodoController.class)
class TodoControllerTest {

	 @Autowired
      private MockMvc mockMvc ;
      
	  @MockBean
      TodoRepository todoRepository ;
      
      @InjectMocks
      private TodoController todoController ;
      
      
	
      @Test
      @WithMockUser(username = "testUser")  
      void testListAllTodos() throws Exception {
        
          when(todoRepository.findByUsername("testUser")).thenReturn(Arrays.asList(
              new Todo(1, "testUser", "Learn Spring", LocalDate.now(), false)
          ));

         
          mockMvc.perform(get("/list-todos"))
              .andExpect(status().isOk())
              .andExpect(view().name("listTodos"))
              .andExpect(model().attributeExists("todos"));
      }
      
      
      
      
      @Test
      @WithMockUser(username = "testUser")  
      void testShowNewTodoPage() throws Exception {
          mockMvc.perform(get("/add-todo"))
              .andExpect(status().isOk())
              .andExpect(view().name("todo"))
              .andExpect(model().attributeExists("todo"));
      }
      
      
      
      @Test
      @WithMockUser(username = "yasmine", roles = {"USER"})
      void testAddNewTodo_successful() throws Exception {
          
    	  
          mockMvc.perform(post("/add-todo")
        		  .with(csrf()) // Include CSRF token
                  .param("description", "Learn MockMvc")
                  .param("targetDate", LocalDate.now().plusDays(10).toString())
                  .param("done", "false")
          )
          .andExpect(MockMvcResultMatchers.redirectedUrl("list-todos"));

          
      }
      
      
      @Test
      @WithMockUser(username = "yasmine", roles = {"USER"})
      void testAddNewTodo_withErrors() throws Exception {
          mockMvc.perform(post("/add-todo")
                  .with(csrf()) 
                  .param("description", "") 
          )
          .andExpect(status().isOk())
          .andExpect(view().name("todo"));
      }
      
      
      @Test
      @WithMockUser(username = "testUser")  
      void testDeleteTodo() throws Exception {
          mockMvc.perform(get("/delete-todo").param("id", "1"))
              .andExpect(MockMvcResultMatchers.redirectedUrl("list-todos"));
      }

      
      
      @Test
      @WithMockUser(username = "testUser")
      void testShowUpdateTodoPage() throws Exception {
     
          when(todoRepository.findById(1)).thenReturn(java.util.Optional.of(
              new Todo(1, "testUser", "Update this Todo", LocalDate.now(), false)
          ));

          mockMvc.perform(get("/update-todo").param("id", "1"))
              .andExpect(status().isOk())
              .andExpect(view().name("todo"))
              .andExpect(model().attributeExists("todo"));
      }

      
      
      
     
      @Test
      @WithMockUser(username = "yasmine", roles = {"USER"})
      void testUpdateTodo_successful() throws Exception {
          mockMvc.perform(post("/update-todo")
                  .with(csrf()) // Include CSRF token
                  .param("description", "Updated description") // Ensure valid input
                  .param("id", "1") // Include ID if necessary
          )
          .andExpect(status().is3xxRedirection()) // Expect 3xx redirection status
          .andExpect(MockMvcResultMatchers.redirectedUrl("list-todos")); // Expect redirection to "list-todos"
      }


     
      
     
      @Test
      @WithMockUser(username = "yasmine", roles = {"USER"})
      void testUpdateTodo_withErrors() throws Exception {
          mockMvc.perform(post("/update-todo")
        		  .with(csrf())
                  .param("description", "")
          )
          .andExpect(status().isOk())
          .andExpect(view().name("todo"));
      }
}
