/*
 * Fixture Monkey
 *
 * Copyright (c) 2021-present NAVER Corp.
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

package com.navercorp.fixturemonkey.api.arbitrary;

import java.util.function.Predicate;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

@API(since = "1.1.12", status = Status.EXPERIMENTAL)
public interface CharacterCombinableArbitrary extends CombinableArbitrary<Character> {
	@Override
	Character combined();

	@Override
	Character rawValue();

	/**
	 * Generates a CharacterCombinableArbitrary which produces characters within the specified range.
	 *
	 * @param min the minimum character value (inclusive)
	 * @param max the maximum character value (inclusive)
	 * @return the CharacterCombinableArbitrary producing characters between {@code min} and {@code max}
	 */
	CharacterCombinableArbitrary withRange(char min, char max);

	/**
	 * Generates a CharacterCombinableArbitrary which produces only alphabetic characters (a-z, A-Z).
	 *
	 * @return the CharacterCombinableArbitrary producing alphabetic characters
	 */
	CharacterCombinableArbitrary alpha();

	/**
	 * Generates a CharacterCombinableArbitrary which produces only numeric characters (0-9).
	 *
	 * @return the CharacterCombinableArbitrary producing numeric characters
	 */
	CharacterCombinableArbitrary numeric();

	/**
	 * Generates a CharacterCombinableArbitrary which produces alphanumeric characters (a-z, A-Z, 0-9).
	 *
	 * @return the CharacterCombinableArbitrary producing alphanumeric characters
	 */
	CharacterCombinableArbitrary alphaNumeric();

	/**
	 * Generates a CharacterCombinableArbitrary which produces only ASCII printable characters.
	 *
	 * @return the CharacterCombinableArbitrary producing ASCII printable characters
	 */
	CharacterCombinableArbitrary ascii();

	/**
	 * Generates a CharacterCombinableArbitrary which produces only uppercase alphabetic characters (A-Z).
	 *
	 * @return the CharacterCombinableArbitrary producing uppercase characters
	 */
	CharacterCombinableArbitrary uppercase();

	/**
	 * Generates a CharacterCombinableArbitrary which produces only lowercase alphabetic characters (a-z).
	 *
	 * @return the CharacterCombinableArbitrary producing lowercase characters
	 */
	CharacterCombinableArbitrary lowercase();

	/**
	 * Generates a CharacterCombinableArbitrary which produces Korean characters (가-힣).
	 *
	 * @return the CharacterCombinableArbitrary producing Korean characters
	 */
	CharacterCombinableArbitrary korean();

	/**
	 * Generates a CharacterCombinableArbitrary which produces emoji characters.
	 *
	 * @return the CharacterCombinableArbitrary producing emoji characters
	 */
	CharacterCombinableArbitrary emoji();

	/**
	 * Generates a CharacterCombinableArbitrary which produces whitespace characters.
	 *
	 * @return the CharacterCombinableArbitrary producing whitespace characters
	 */
	CharacterCombinableArbitrary whitespace();

