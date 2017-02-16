#pragma once

/**
 * Parses one file.
 */
void parse_file(char *filename);

/**
 * Parses one line and inserts it into the root AST.
 */
void parse_line(char *line);
