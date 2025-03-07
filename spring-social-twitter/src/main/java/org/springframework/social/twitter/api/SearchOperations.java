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
package org.springframework.social.twitter.api;

import java.util.List;

import org.springframework.social.ApiException;
import org.springframework.social.MissingAuthorizationException;


/**
 * Interface defining the operations for searching Twitter and retrieving trending data.
 * @author Craig Walls
 */
public interface SearchOperations {
	
	/**
	 * Searches Twitter, returning the first 50 matching {@link Tweet}s
	 * @param query The search query string
	 * @return a {@link SearchResults} containing the search results metadata and a list of matching {@link Tweet}s
	 * @throws ApiException if there is an error while communicating with Twitter.
	 * @see SearchResults
	 * @see Tweet
	 */
	SearchResults search(String query);

	/**
	 * Searches Twitter, returning a specific page out of the complete set of results.
	 * @param query The search query string
	 * @param page The page to return
	 * @param pageSize The number of {@link Tweet}s per page
	 * @return a {@link SearchResults} containing the search results metadata and a list of matching {@link Tweet}s
	 * @throws ApiException if there is an error while communicating with Twitter.
	 * @see SearchResults
	 * @see Tweet
	 */
	SearchResults search(String query, int page, int pageSize);

	/**
	 * Searches Twitter, returning a specific page out of the complete set of
	 * results. Results are filtered to those whose ID falls between sinceId and maxId.
	 * @param query The search query string
	 * @param page The page to return
	 * @param pageSize The number of {@link Tweet}s per page
	 * @param sinceId The minimum {@link Tweet} ID to return in the results
	 * @param maxId The maximum {@link Tweet} ID to return in the results
     * @param geocode the geocode
     * @param resultType the result type
	 * @return a {@link SearchResults} containing the search results metadata and a list of matching {@link Tweet}s
	 * @throws ApiException if there is an error while communicating with Twitter.
	 * @see SearchResults
	 * @see Tweet
	 */
	SearchResults search(String query, int page, int pageSize, long sinceId, long maxId, String geocode, String resultType);
	
	/**
	 * Retrieves the authenticating user's saved searches.
	 * @return a list of SavedSearch items
	 * @throws ApiException if there is an error while communicating with Twitter.
	 * @throws MissingAuthorizationException if TwitterTemplate was not created with OAuth credentials.
	 */
	List<SavedSearch> getSavedSearches();
	
	/**
	 * Retrieves a single saved search by the saved search's ID.
	 * @param searchId the ID of the saved search
	 * @return a SavedSearch
	 * @throws ApiException if there is an error while communicating with Twitter.
	 * @throws MissingAuthorizationException if TwitterTemplate was not created with OAuth credentials.
	 */
	SavedSearch getSavedSearch(long searchId);

	/**
	 * Creates a new saved search for the authenticating user.
	 * @param query the search query to save
	 * @return the SavedSearch
	 * @throws ApiException if there is an error while communicating with Twitter.
	 * @throws MissingAuthorizationException if TwitterTemplate was not created with OAuth credentials.
	 */
	SavedSearch createSavedSearch(String query);

	/**
	 * Deletes a saved search
	 * @param searchId the ID of the saved search
	 * @throws ApiException if there is an error while communicating with Twitter.
	 * @throws MissingAuthorizationException if TwitterTemplate was not created with OAuth credentials.
	 */
	void deleteSavedSearch(long searchId);

	/**
	 * Retrieves the top 20 trending topics, hourly for the past 24 hours.
	 * This list includes hashtagged topics.
	 * @return a list of Trends objects, one for each hour in the past 24 hours, ordered with the most recent hour first.
	 * @throws ApiException if there is an error while communicating with Twitter.
	 */
	List<Trends> getDailyTrends();

	/**
	 * Retrieves the top 20 trending topics, hourly for the past 24 hours.
	 * @param excludeHashtags if true, hashtagged topics will be excluded from the trends list.
	 * @return a list of Trends objects, one for each hour in the past 24 hours, ordered with the most recent hour first.
	 * @throws ApiException if there is an error while communicating with Twitter.
	 */
	List<Trends> getDailyTrends(boolean excludeHashtags);
	
	/**
	 * Retrieves the top 20 trending topics, hourly for a 24-hour period starting at the specified date.
	 * @param excludeHashtags if true, hashtagged topics will be excluded from the trends list.
	 * @param startDate the date to start retrieving trending data for, in MM-DD-YYYY format.
	 * @return a list of Trends objects, one for each hour in the given 24 hours, ordered with the most recent hour first.
	 * @throws ApiException if there is an error while communicating with Twitter.
	 */
	List<Trends> getDailyTrends(boolean excludeHashtags, String startDate);

	/**
	 * Retrieves the top 30 trending topics for each day in the past week.
	 * This list includes hashtagged topics.
	 * @return a list of Trends objects, one for each day in the past week, ordered with the most recent day first.
	 * @throws ApiException if there is an error while communicating with Twitter.
	 */
	List<Trends> getWeeklyTrends();

	/**
	 * Retrieves the top 30 trending topics for each day in the past week.
	 * @param excludeHashtags if true, hashtagged topics will be excluded from the trends list.
	 * @return a list of Trends objects, one for each day in the past week, ordered with the most recent day first.
	 * @throws ApiException if there is an error while communicating with Twitter.
	 */
	List<Trends> getWeeklyTrends(boolean excludeHashtags);
	
	/**
	 * Retrieves the top 30 trending topics for each day in a given week.
	 * @param excludeHashtags if true, hashtagged topics will be excluded from the trends list.
	 * @param startDate the date to start retrieving trending data for, in MM-DD-YYYY format.
	 * @return a list of Trends objects, one for each day in the given week, ordered with the most recent day first.
	 * @throws ApiException if there is an error while communicating with Twitter.
	 */
	List<Trends> getWeeklyTrends(boolean excludeHashtags, String startDate);

	/**
	 * Retrieves the top 10 trending topics for a given location, identified by its "Where on Earth" (WOE) ID.
	 * This includes hashtagged topics.
	 * See http://developer.yahoo.com/geo/geoplanet/guide/concepts.html for more information on WOE.
	 * @param whereOnEarthId the Where on Earth ID for the location to retrieve trend data.
	 * @return A Trends object with the top 10 trending topics for the location.
	 * @throws ApiException if there is an error while communicating with Twitter.
	 */
	Trends getLocalTrends(long whereOnEarthId);

	/**
	 * Retrieves the top 10 trending topics for a given location, identified by its "Where on Earth" (WOE) ID.
	 * See http://developer.yahoo.com/geo/geoplanet/guide/concepts.html for more information on WOE.
	 * @param whereOnEarthId the Where on Earth ID for the location to retrieve trend data.
	 * @param excludeHashtags if true, hashtagged topics will be excluded from the trends list.
	 * @return A Trends object with the top 10 trending topics for the given location.
	 * @throws ApiException if there is an error while communicating with Twitter.
	 */
	Trends getLocalTrends(long whereOnEarthId, boolean excludeHashtags);

}
