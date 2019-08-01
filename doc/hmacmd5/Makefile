main:infra_md5.o main.o
	gcc infra_md5.o main.o -o main

infra_md5.o:infra_md5.c
	gcc -c infra_md5.c
main.o:main.c
	gcc -c main.c

.PHONY:clean
clean:
	rm *.o
