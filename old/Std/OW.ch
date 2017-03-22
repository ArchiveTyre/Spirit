/*
********************************************************************************
* Std.OW.ch                                                                    *
* -----------------------------------------------------------------------------*
* By: Tyrerexus                                                                *
* License:                                                                     *
* Date: 19 March 2017                                                          *
* Description:                                                                 *
*       This file class defines interaction between the                        *
*       program and the outer world.                                           *
********************************************************************************
*/

import Std.IO

/** The input and output class. */
var IO io

/** Default construct for main. */
fun construct:

	/** Create the IO object for accessing the outer world. */
	io = new IO()
