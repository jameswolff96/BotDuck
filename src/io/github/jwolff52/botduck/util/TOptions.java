/*	  It's a Twitch bot, because we can.
 *    Copyright (C) 2015  Logan Saso, James Wolff, Kyle Nabinger
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.jewsofhazard.pcmrbot.util;

public enum TOptions {

	welcomeMessage("welcomeMessage"), numCaps("numCaps"), numSymbols(
			"numSymbols"), numEmotes("numEmotes"), paragraphLength(
			"paragraphLength"), link("link"), regular("regular");

	private final String optionID;

	/**
	 * @return the optionID in text form
	 */
	public String getOptionID() {
		return optionID;
	}

	/**
	 * @param id - option ID in text form
	 */
	TOptions(String id) {
		optionID = id;
	}
}
