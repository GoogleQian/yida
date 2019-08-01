#include<stdio.h>
#include<string.h>
#include "infra_md5.h"


void utils_hmac_md5(const char *msg, int msg_len, char *digest, const char *key, int key_len);


void main(){
	  char *chipId = "123456789012345";

          char sign[36] = {'\0'};

          char *password = "xiaohe";	  

	  utils_hmac_md5(chipId, strlen(chipId), sign, password, strlen(password)); 

	  printf("%s\n", sign);
}

