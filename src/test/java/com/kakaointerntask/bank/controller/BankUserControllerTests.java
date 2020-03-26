package com.kakaointerntask.bank.controller;

import com.kakaointerntask.bank.controller.entity.BankUserController;
import com.kakaointerntask.bank.service.BankUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles("test")
@WebMvcTest(BankUserController.class)
class BankUserControllerTests {
	@Autowired
	private MockMvc mvc;
	@MockBean
	private BankUserService bankUserService;

	@Test
	public void testSaveBankUsers() throws Exception {
	/*	List<BankUser> articles = Arrays.asList(
				new BankUser(1, "kwseo", "good", "good content", now()),
				new BankUser(2, "kwseo", "haha", "good haha", now()));

		given(bankUserService.findBankUserById(eq("kwseo"))).willReturn(articles);

		mvc.perform(get("/api/articles?author=kwseo"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("@[*].author", containsInAnyOrder("kwseo", "kwseo")));*/
	}

	private Timestamp now() {
		return new Timestamp(System.currentTimeMillis());
	}
}
