# import stdio
# "stdio" is not a standard Python library and looks like an absolute disaster on the Princeton site
# Better practice would to be using language standard "print"
import sys

# Accept name (str) and age (str) as command-line arguments.
name = str(sys.argv[1])
age = str(sys.argv[2])

# Write the message "name is age years old." to standard output.
print(name + " is " + age + " years old.")
# stdio.writeln(name + " is " + age + " years old.")
