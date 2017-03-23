################################
#
# Configureations:
#

# Compilerd to use.
CPP_COMPILER=clang++
C_COMPILER=clang
LD=clang++

# Libs
LIBS= pthread

# Uncomment if release build.
DEBUG= true

# Flags to be passed on to the C compiler.
COMMON_FLAGS= -Wall -I.
C_FLAGS=
CPP_FLAGS= -std=c++0x
EXTRA_C_FLAGS=
LD_FLAGS=

# Put anything here for it to be configured by pkg-config.
PKG_LIBS=

# Folders:
# The directory where output will be placed...
OBJDIR=obj

# All directories where source that should be compiled is placed.
SRCDIRS=src

################################
#
# Info:
#
# MaiMake v0
#
# Made by: Alexander Björkman 2016
# Version: 0.1.3.1
# Description:
# Just drop it into any folder and an executable
# will be created using source code from the src/ dir.
# NOTE: The name of the project is determined by the folder name.
# History:
# 0.1.4 (Feb 16 2017):
# 	* Now supports multiple source directories!
# 	* Added color to output.
# 	* Prettified the output.
# 	* Also simplified the code a little bit.
# 0.1.3.1 (Nov 3 2016):
#	Now supports test rule which compiles with
#	TEST macro set to 1. /Alexander Björkman
# 0.1.3 (Oct 24 2016):
#	Now supprots C++ /Alexander Björkman
# 0.1.2 (???):
#	Now supports multiple directories in src. /Alexander Björkman
# 0.1.1 (???):
#	Now supports libs with pkg-config /Alexander Björkman
# 0.1.0 (???):
#	Project started. Compiles code with automatic detection.
#
################################
#
# Code:
#

# Colors
export ccred:=$(shell tput setaf 1)
export ccgreen:=$(shell tput setaf 2)
export ccyellow:=$(shell tput setaf 3)
export ccend:=$(shell tput sgr0)

# If debug is set then there are some additional flags.
ifdef DEBUG
	COMMON_FLAGS+= -O0 -g -DDEBUG
	BISON_FLAGS+= -t
	#FLEX_FLAGS+= -d
else
	COMMON_FLAGS+= -O3
	FLEX_FLAGS+= -Cfr
endif

# If PKG_LIBS is set then execute pkg-config.
ifdef PKG_LIBS
	PKG_OUTPUT=$(shell pkg-config --cflags --libs $(PKG_LIBS))
	COMMON_FLAGS+=$(PKG_OUTPUT)
endif

# Now generate the final flags.
C_FLAGS+= $(EXTRA_C_FLAGS)
C_FLAGS+= $(COMMON_FLAGS)
CPP_FLAGS+= $(EXTRA_CPP_FLAGS)
CPP_FLAGS+= $(COMMON_FLAGS)
LD_FLAGS+= $(COMMON_FLAGS)
BISON_FLAGS+=
FLEX_FLAGS+=

# Get the target name.
CASEEXEC=$(shell basename $(CURDIR))
EXEC=$(shell echo $(CASEEXEC) | tr A-Z a-z)

# Path of test executable which should be stored inside of "$(OBJDIR)/test".
TEST_EXEC=testself

# Get sources to compile.
BISON_SOURCES+=$(foreach dir,$(SRCDIRS),$(shell find $(dir)/ -name "*.y"))
FLEX_SOURCES+=$(foreach dir,$(SRCDIRS),$(shell find $(dir)/ -name "*.l"))
C_SOURCES+=$(foreach dir,$(SRCDIRS),$(shell find $(dir)/ -name "*.c"))
CPP_SOURCES+=$(foreach dir,$(SRCDIRS),$(shell find $(dir)/ -name "*.cpp"))

# Create a list of ojbects that need to be built.
# Then append objdir to the object names.
C_OBJECTS=$(addprefix $(OBJDIR)/, $(addsuffix .o, $(basename $(C_SOURCES))))
CPP_OBJECTS=$(addprefix $(OBJDIR)/, $(addsuffix .o, $(basename $(CPP_SOURCES))))
BISON_OBJECTS=$(addprefix $(OBJDIR)/, $(addsuffix .o, $(basename $(BISON_SOURCES))))
FLEX_OBJECTS=$(addprefix $(OBJDIR)/, $(addsuffix .o, $(basename $(FLEX_SOURCES))))

ALL_OBJECTS:= $(BISON_OBJECTS) $(FLEX_OBJECTS) $(C_OBJECTS) $(CPP_OBJECTS)

# To make sure that the directory tree of all the src directroies exists in the build directory
SUBSRCDIRS+=$(foreach dir,$(SRCDIRS),$(shell find $(dir)/ -type d))

# Add a prefix to the folders.
DIRECTORIES=$(addprefix $(OBJDIR)/, $(SUBSRCDIRS))


# Include the libs...
LD_FLAGS+=$(addprefix -l,$(LIBS))

# Add the SRCDIRS to the include path.
C_FLAGS+=$(foreach dir,$(SRCDIRS),-I$(dir) )
C_FLAGS+=$(foreach dir,$(DIRECTORIES),-I$(dir) )

CPP_FLAGS+=$(foreach dir,$(SRCDIRS),-I$(dir) )
CPP_FLAGS+=$(foreach dir,$(DIRECTORIES),-I$(dir) )

# The goal is to get the executable.
all: $(EXEC)
	@echo "$(ccgreen)Done!$(ccend)"

# The test rule works by calling the makefile again
# but with a custom flag and obj dir.
# We later call the executable created.
test:
	make $(TEST_EXEC) COMMON_FLAGS="-DTEST" OBJDIR="obj/test"
	./$(OBJDIR)/test/$(TEST_EXEC)


# To clean we simply remove everything that is generated.
clean:
	rm -rf $(OBJDIR)

# Self explanatory.
help:
	@echo "Type: 'make all'"
	@echo "	To build everthing."
	@echo "Type: 'make clean'"
	@echo "	To clean after build"
	@echo "Type: 'make help'"
	@echo "	For this help message."

# The executable needs the object files.
# To create the executable we execute the linker
$(EXEC): $(DIRECTORIES) $(ALL_OBJECTS)
	$(LD) $(ALL_OBJECTS) $(LD_FLAGS) -o $@
	@echo "$(ccgreen)DONE: Linking executable."
	@echo "$(ccend)"

$(TEST_EXEC): $(DIRECTORIES) $(ALL_OBJECTS)
	$(LD) $(ALL_OBJECTS) $(LD_FLAGS) -o $(OBJDIR)/$@

# Automatic dependency graph generation
-include $(C_OBJECTS:.o=.d)
-include $(CPP_OBJECTS:.o=.d)
-include $(BISON_OBJECTS:.o=.y.d)
-include $(FLEX_OBJECTS:.o=.l.d)

# Here we compile a object file using it's c file partner.
$(OBJDIR)/%.o: %.cpp $(BISON_OBJECTS)
	$(CPP_COMPILER) $(CPP_FLAGS) -c -MMD -MT $@ -MF $(patsubst %.o,%.d,$@) $< -o $@
$(OBJDIR)/%.o: %.c $(BISON_OBJECTS)
	$(C_COMPILER) $(C_FLAGS) -c -MMD -MT $@ -MF $(patsubst %.o,%.d,$@) $< -o $@
$(OBJDIR)/%.o: %.y
	bison $(BISON_FLAGS) --defines=$(patsubst %.o,%.tab.hpp,$@) --output=$(patsubst %.o,%.tab.cpp,$@) $<
	$(CPP_COMPILER) $(CPP_FLAGS) -Wno-writable-strings -c -MMD -MT $@ -MF $(patsubst %.o,%.y.d,$@) $(patsubst %.o,%.tab.cpp,$@) -o $@
$(OBJDIR)/%.o: %.l $(BISON_OBJECTS)
	flex $(FLEX_FLAGS) --outfile=$(patsubst %.o,%.yy.cpp,$@) $<
	$(CPP_COMPILER) $(CPP_FLAGS) -Wno-writable-strings -c -MMD -MT $@ -MF $(patsubst %.o,%.l.d,$@) $(patsubst %.o,%.yy.cpp,$@) -o $@


# Make sure the sub-folders in src/ exists in obj/.
$(DIRECTORIES):
	@echo "$(ccyellow)Creating directories:"
	@$(foreach dir,$(DIRECTORIES),mkdir -p $(dir);echo $(dir);)
	@echo "$(ccend)"

################################
#
# Explanation:
#
# $@ is what you are trying to create.
# $< Is the first requirement me thinks.
# what-to-create: dependencies
#	[tab] system commands
#
# SOMETHING=$(SOURCE:.a=.b)
# In this example SOMETHING becomes everything SOURCE is
# but .a is replaced by .b
#
# EVERYTHING=$(wildcard *.xyz)
# Here EVERYTHING becomes all list of files matching
# the regex. In this case the regex is "*.xyz"
#
# Note that the "all" rule is the default rule.
#
# Any system command with a "@" prefix is quiet.
#
# Note that the prefix "-" in "-include" allows the
# system command to fail. In other words not fin the .d file.
