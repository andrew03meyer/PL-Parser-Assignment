int num, sum, flag;
sum := 0;
num := 1;
flag := 1;
while num < 6 do
    if num > 3 then
            sum := sum + 2;
    else
        sum := sum + 1;
    fi;
    num := num + 1;
od;
print sum;