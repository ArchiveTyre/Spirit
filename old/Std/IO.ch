/*
********************************************************************************
* Std.IO.ch                                                                    *
* -----------------------------------------------------------------------------*
* By: Tyrerexus                                                                *
* License:                                                                     *
* Date: 19 March 2017                                                          *
* Description:                                                                 *
*       This file defines some basic function for                              *
*       IO access.                                                             *
********************************************************************************
*/

#inline

/* Include stdio from c. */
#include <stdio.h>

/* Declare our string type. */
/* FIXME: This will be replaced in the future. */
typedef char* string;

#endinline

fun print(string s):
	#inline
	printf(s);
	#endinline

fun printLine(string s):
	#inline
	puts(s);
	#endinline
