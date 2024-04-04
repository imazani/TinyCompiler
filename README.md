# TinyCompiler

A compiler written in Java for a simplified dialect of the language BASIC that compiles the source code to C.

Example Input:
```
PRINT "How many fibonacci numbers do you want?"
INPUT nums
PRINT ""

LET a = 0
LET b = 1
WHILE nums > 0 REPEAT
    PRINT a
    LET c = a + b
    LET a = b
    LET b = c
    LET nums = nums - 1
ENDWHILE
```

Example Output
```
#include <stdio.h>
int main(void) {
float a;
float b;
printf("A simple code example\n");
a=11;
b=10;
while(a>b) {
printf("%.2f\n", (float)(a));
printf("%.2f\n", (float)(b));
a=a-1;
}
return 0;
}
```


Features supported:
1. Basic arithmetic
2. If-Statements
3. While-Loops
4. Print to console
5. Input numbers

Features to be added:
1. Add Abstract Syntax Tree Representation
2. Additional Primitive Types (Boolean, Ints)
3. Additional Test Cases

### Compiler Design Diagram
![image](https://github.com/imazani/TinyCompiler/assets/88912589/b730ab16-8921-4272-9aa2-63418281fe6d)
