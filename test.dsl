int ch, num;
ch := 0;
num := 2;

while ch < 128
do
    if(num < ch)
    then
        while num <= 2
        do
            num := 2;
            num := 3;
        od;
    else
        num := 3;
    fi;
od;