/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.social.twitter.api.impl;

import static org.junit.Assert.*;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.social.test.client.RequestMatchers.*;
import static org.springframework.social.test.client.ResponseCreators.*;

import java.util.List;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.social.NotAuthorizedException;
import org.springframework.social.twitter.api.DirectMessage;
import org.springframework.social.twitter.api.MessageTooLongException;

/**
 * @author Craig Walls
 */
public class DirectMessageTemplateTest extends AbstractTwitterApiTest {

	@Test
	public void getDirectMessagesReceived() {
		mockServer.expect(requestTo("https://api.twitter.com/1/direct_messages.json?page=1&count=20"))
				.andExpect(method(GET))
				.andRespond(withResponse(jsonResource("messages"), responseHeaders));

		List<DirectMessage> messages = twitter.directMessageOperations().getDirectMessagesReceived();
		assertDirectMessageListContents(messages);
	}
	
	@Test
	public void getDirectMessagesReceived_paged() {
		mockServer.expect(requestTo("https://api.twitter.com/1/direct_messages.json?page=3&count=12"))
				.andExpect(method(GET))
				.andRespond(withResponse(jsonResource("messages"), responseHeaders));

		List<DirectMessage> messages = twitter.directMessageOperations().getDirectMessagesReceived(3, 12);
		assertDirectMessageListContents(messages);
	}

	@Test
	public void getDirectMessagesReceived_paged_withSinceIdAndMaxId() {
		mockServer.expect(requestTo("https://api.twitter.com/1/direct_messages.json?page=3&count=12&since_id=112233&max_id=332211"))
				.andExpect(method(GET))
				.andRespond(withResponse(jsonResource("messages"), responseHeaders));

		List<DirectMessage> messages = twitter.directMessageOperations().getDirectMessagesReceived(3, 12, 112233, 332211);
		assertDirectMessageListContents(messages);
	}

	@Test(expected = NotAuthorizedException.class)
	public void getDirectMessagesReceived_unauthorized() {
		unauthorizedTwitter.directMessageOperations().getDirectMessagesReceived();
	}

	@Test
	public void getDirectMessagesSent() {
		mockServer.expect(requestTo("https://api.twitter.com/1/direct_messages/sent.json?page=1&count=20"))
				.andExpect(method(GET))
				.andRespond(withResponse(jsonResource("messages"), responseHeaders));

		List<DirectMessage> messages = twitter.directMessageOperations().getDirectMessagesSent();
		assertDirectMessageListContents(messages);
	}

	@Test
	public void getDirectMessagesSent_paged() {
		mockServer.expect(requestTo("https://api.twitter.com/1/direct_messages/sent.json?page=3&count=25"))
				.andExpect(method(GET))
				.andRespond(withResponse(jsonResource("messages"), responseHeaders));

		List<DirectMessage> messages = twitter.directMessageOperations().getDirectMessagesSent(3, 25);
		assertDirectMessageListContents(messages);
	}

	@Test
	public void getDirectMessagesSent_paged_withSinceIdAndMaxId() {
		mockServer.expect(requestTo("https://api.twitter.com/1/direct_messages/sent.json?page=3&count=25&since_id=2468&max_id=8642"))
				.andExpect(method(GET))
				.andRespond(withResponse(jsonResource("messages"), responseHeaders));

		List<DirectMessage> messages = twitter.directMessageOperations().getDirectMessagesSent(3, 25, 2468, 8642);
		assertDirectMessageListContents(messages);
	}

	@Test(expected = NotAuthorizedException.class)
	public void getDirectMessagesSent_unauthorized() {
		unauthorizedTwitter.directMessageOperations().getDirectMessagesSent();
	}

	@Test
	public void getDirectMessage() {
		mockServer.expect(requestTo("https://api.twitter.com/1/direct_messages/show/23456.json"))
			.andExpect(method(GET))
			.andRespond(withResponse(jsonResource("directMessage"), responseHeaders));
		DirectMessage message = twitter.directMessageOperations().getDirectMessage(23456);
		assertSingleDirectMessage(message);
	}
	
	@Test
	public void sendDirectMessage_toScreenName() {
		mockServer.expect(requestTo("https://api.twitter.com/1/direct_messages/new.json")).andExpect(method(POST))
				.andExpect(body("screen_name=habuma&text=Hello+there%21"))
				.andRespond(withResponse(jsonResource("directMessage"), responseHeaders));
		DirectMessage message = twitter.directMessageOperations().sendDirectMessage("habuma", "Hello there!");
		assertSingleDirectMessage(message);
		mockServer.verify();
	}

	@Test(expected = MessageTooLongException.class)
	public void sendDirectMessage_toScreenName_tooLong() {
		mockServer.expect(requestTo("https://api.twitter.com/1/direct_messages/new.json")).andExpect(method(POST))
				.andExpect(body("screen_name=habuma&text=Really+long+message"))
			.andRespond(withResponse("{\"error\":\"There was an error sending your message: The text of your direct message is over 140 characters.\"}", responseHeaders, HttpStatus.FORBIDDEN, ""));		
		twitter.directMessageOperations().sendDirectMessage("habuma", "Really long message");
		mockServer.verify();
	}

	@Test(expected = NotAuthorizedException.class)
	public void sendDirectMessaage_toScreenName_unauthorized() {
		unauthorizedTwitter.directMessageOperations().sendDirectMessage("habuma", "Hello there!");
	}

	@Test
	public void sendDirectMessage_toUserId() {
		mockServer.expect(requestTo("https://api.twitter.com/1/direct_messages/new.json")).andExpect(method(POST))
				.andExpect(body("user_id=11223&text=Hello+there%21"))
				.andRespond(withResponse(jsonResource("directMessage"), responseHeaders));
		DirectMessage message = twitter.directMessageOperations().sendDirectMessage(11223, "Hello there!");
		assertSingleDirectMessage(message);
		mockServer.verify();
	}

	private void assertSingleDirectMessage(DirectMessage message) {
		assertEquals(23456, message.getId());
		assertEquals("Back at ya", message.getText());
		assertEquals(13579, message.getSender().getId());
		assertEquals("kdonald", message.getSender().getScreenName());
		assertEquals(24680, message.getRecipient().getId());
		assertEquals("rclarkson", message.getRecipient().getScreenName());
	}
	
	@Test(expected = NotAuthorizedException.class)
	public void sendDirectMessaage_toUserId_unauthorized() {
		unauthorizedTwitter.directMessageOperations().sendDirectMessage(112233, "Hello there!");
	}
	
	@Test
	public void deleteDirectMessage() {
		mockServer.expect(requestTo("https://api.twitter.com/1/direct_messages/destroy/42.json"))
				.andExpect(method(DELETE))
				.andRespond(withResponse(jsonResource("directMessage"), responseHeaders));
		twitter.directMessageOperations().deleteDirectMessage(42L);
		mockServer.verify();
	}

	@Test(expected = NotAuthorizedException.class)
	public void deleteDirectMessage_unauthorized() {
		unauthorizedTwitter.directMessageOperations().deleteDirectMessage(42L);
	}
	
	private void assertDirectMessageListContents(List<DirectMessage> messages) {
		assertEquals(2, messages.size());
		assertEquals(12345, messages.get(0).getId());
		assertEquals("Hello there", messages.get(0).getText());
		assertEquals(24680, messages.get(0).getSender().getId());
		assertEquals("rclarkson", messages.get(0).getSender().getScreenName());
		assertEquals(13579, messages.get(0).getRecipient().getId());
		assertEquals("kdonald", messages.get(0).getRecipient().getScreenName());
		// assertTimelineDateEquals("Tue Jul 13 17:38:21 +0000 2010", messages.get(0).getCreatedAt());
		assertEquals(23456, messages.get(1).getId());
		assertEquals("Back at ya", messages.get(1).getText());
		assertEquals(13579, messages.get(1).getSender().getId());
		assertEquals("kdonald", messages.get(1).getSender().getScreenName());
		assertEquals(24680, messages.get(1).getRecipient().getId());
		assertEquals("rclarkson", messages.get(1).getRecipient().getScreenName());
	}

}
