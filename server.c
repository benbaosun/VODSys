/*erver.c  
linux 下socket网络编程简例  - 服务端程序 
服务器端口设为 0x8888   （端口和地址可根据实际情况更改，或者使用参数传入） 
服务器地址设为 
作者:golden1314521#gmail.com (将#换为@) 
*/  
  
#include <stdlib.h>   
#include <sys/types.h>   
#include <stdio.h>   
#include <sys/socket.h>   
#include <linux/in.h>   
#include <string.h>
#include <mysql.h>
#include <string.h>
#include <assert.h>


int init_sfp(unsigned short);
void response(char *);
//void getURL(char *);

  
int main()  
{  
    int sfp;   
    unsigned short port = 0x8888;   
    

    if(-1 == (sfp=init_sfp(port)))  
    {  
        printf("init_socket fail ! \r\n");  
        return -1;  
    }  
    if(-1 == listen(sfp,5))  
    {  
        printf("listen fail !\r\n");  
        return -1;  
    } else {
        printf("Server started,port:%d\r\n",port);  
    }    

  
    while(1)  
    {
          
        struct sockaddr_in c_add;
      	int sin_size = sizeof(struct sockaddr_in);  
	int nfp = accept(sfp, (struct sockaddr *)(&c_add), &sin_size);  

        printf("\n**********************************************\n\n");

        char buffer[1024]={0};    /* 接受缓冲区 */
        int recbytes;
        if(-1 == nfp)  
        {  
            printf("accept fail !\r\n");  
            return -1;  
        }  
        printf("accept ok!\r\n");  
  
        if(-1 == (recbytes = read(nfp,buffer,1024)))
        {
            printf("read data fail !\r\n");
            return -1;
        }
        printf("read ok\rthe num of bytes is %d:\r\n",recbytes);

        buffer[recbytes]='\0';
        printf("request from client : %s\r\n",buffer);

        /* 这里使用write向客户端发送信息，也可以尝试使用其他函数实现 */
        response(buffer);
	printf("write to client : %s\r\n",buffer);
        if(-1 == write(nfp,buffer,1024))  
        {  
            printf("write fail!\r\n");  
            return -1;
        } 
        printf("write ok!\r\n");  
        //}  

        close(nfp);  
        printf("\n**********************************************\n\n");
  
    }  

    close(sfp);  
    return 0;  
} 



 
int init_sfp(unsigned short port) {
    int sfp;
    unsigned short portnum=port;/*端服务器使用端口 */  
    struct sockaddr_in s_add;

    //printf("Hello,welcome to my server !\r\n%d\n",portnum);  
    sfp = socket(AF_INET, SOCK_STREAM, 0);  
    if(-1 == sfp)  
    {    
        return -1;  
    }    
  
    /* 填充服务器端口地址信息，以便下面使用此地址和端口监听 */  
    bzero(&s_add,sizeof(struct sockaddr_in));  
    s_add.sin_family=AF_INET;  
    s_add.sin_addr.s_addr=htonl(INADDR_ANY); /* 这里地址使用全0，即所有 */  
    s_add.sin_port=htons(portnum);  
    /* 使用bind进行绑定端口 */  
    if(-1 == bind(sfp,(struct sockaddr *)(&s_add), sizeof(struct sockaddr)))  
    {  
        return -1;  
    }  
    
    return sfp;  
}

   
void response(char *packets){

    MYSQL *conn = NULL;
    MYSQL_RES *res = NULL;
    MYSQL_ROW row;
   
    char *server = "localhost";
    char *user = "root";
    char *password = "jobs1991"; /* 此处改成你的密码 */
    char *database = "moviedatabase";

    conn = mysql_init(NULL);

   /* Connect to database */
   if (!mysql_real_connect(conn, server,
          user, password, database, 0, NULL, 0)) {
      fprintf(stderr, "%s\n", mysql_error(conn));
     // return 1;
   }

   /* send SQL query */
   
   int utf8;
   utf8=mysql_query(conn,"set character_set_results=utf8");
   //printf("utf8=%d\n",utf8);
   assert(utf8==0);          //其实你需要的就是这行上面的部分 然后你就什么都会了  ^_^    这四行代码以上和以下的代码几乎都是没有意义的，因为这不是个问题
   //mysql_set_character_set(conn,"utf-8");
   if(strstr(packets,"AllSong")) {
	//printf("buffer is to be clear\n");
	memset(packets,0,1024);
	//printf("buffer is clear\n");
   	if (mysql_query(conn, "SELECT name from streamMovieList")) {
      	fprintf(stderr, "%s\n", mysql_error(conn));
      	//return 1;
   	}

    	res = mysql_use_result(conn);

   	/* output table name */
  	// printf("MySQL Tables in mysql database:\n");
  	while ((row = mysql_fetch_row(res)) != NULL)
   	{
       		strcat(packets,row[0]);
      		strcat(packets,"?");
   	}
   } else if(strstr(packets,"URL")) {
	char * strURL = packets+4;
	//printf("%s\n",strURL);
	char selectURL[100] = "";
	strcat(selectURL,strURL);
        if (mysql_query(conn, "SELECT URL FROM streamMovieList")) {
        	fprintf(stderr, "%s\n", mysql_error(conn));
        	//return 1;
        }

        res = mysql_use_result(conn);
	memset(packets,0,1024);



  	while ((row = mysql_fetch_row(res)) != NULL)

   	{	
		if(strstr(row[0],selectURL)) {
       			strcat(packets,row[0]);
      			strcat(packets,"?");
			break;
		}
   	}
	
   } else if(strstr(packets,"Login")) {
	char username[20];
	char * code;
	char * ptr;
        char * strLogin = packets+6;
	ptr = strstr(strLogin,"?");
	int num = ptr-strLogin;
        strncpy(username,strLogin,num);
	username[num] = 0;
	code = ptr+1;
        if (mysql_query(conn, "SELECT user,code,nickname,email FROM userInfoList")) {
                fprintf(stderr, "%s\n", mysql_error(conn));
        }

        res = mysql_use_result(conn);
        memset(packets,0,1024);

        while ((row = mysql_fetch_row(res)) != NULL)
        {       
		char usertmp[20];
		usertmp[0]=0;
		strcat(usertmp,row[0]);
		char codetmp[20];
		codetmp[0] =0;
		strcat(codetmp,row[1]);
		if(strstr(codetmp,code)&&(strstr(usertmp,username))) 
		{
			//printf("The same\n");
       			strcat(packets,"LoginOK?");
			strcat(packets,username);
			strcat(packets,"?");
			strcat(packets,code);
			strcat(packets,"?");
			strcat(packets,row[2]);
			strcat(packets,"?");
			strcat(packets,row[3]);
			break;
		}	
        
        }

   } else if(strstr(packets,"Register")) {
	char username[20];
	char code[20];
	char nickname[20];
	char * email;
	int num;

	char * ptr;
        char * strLogin = packets+9;

	ptr = strstr(strLogin,"?");
	num = ptr-strLogin;
        strncpy(username,strLogin,num);
	username[num] = 0;

	strLogin = ptr+1;
        ptr = strstr(strLogin,"?");
        num = ptr-strLogin;
        strncpy(code,strLogin,num);
	code[num] = 0;

        strLogin = ptr+1;
        ptr = strstr(strLogin,"?");
        num = ptr-strLogin;
        strncpy(nickname,strLogin,num);
	nickname[num] = 0;

	email = ptr+1;
	//printf("Register:%s,%s,%s,%s\n",username,code,nickname,email);
	
        char sqlInsert[100];
	memset(sqlInsert,0,100);
        strcat(sqlInsert,"INSERT INTO userInfoList(user,code,nickname,email) VALUES('");
	strcat(sqlInsert,username); 
	strcat(sqlInsert,"','");
	strcat(sqlInsert,code); 
	strcat(sqlInsert,"','");
	strcat(sqlInsert,nickname);
	strcat(sqlInsert,"','");
	strcat(sqlInsert,email);
	strcat(sqlInsert,"')");
	printf("%s\n",sqlInsert);


        memset(packets,0,1024);
        if (mysql_query(conn, sqlInsert)) {
		strcat(packets,"RegisterFalse");
                fprintf(stderr, "%s\n", mysql_error(conn));
        } else {
		strcat(packets,"RegisterOK");
	}
	strcat(packets,"?");
   }  else if(strstr(packets,"VideoInfo")) {

        char strURL[100];
	strURL[0] = 0;
	strcat(strURL,packets+10);

        memset(packets,0,1024);
        char sqlVideoInfo[100];
	sqlVideoInfo[0] = 0;
	strcat(sqlVideoInfo,"SELECT name,author,description,URL FROM streamMovieList");


        if (mysql_query(conn, sqlVideoInfo)) {
                fprintf(stderr, "%s\n", mysql_error(conn));
        }
        res = mysql_use_result(conn);
	////////////////////////////////////////////

        while ((row = mysql_fetch_row(res)) != NULL)
        {       
		char URLtmp[100];
		URLtmp[0] = 0;
		strcat(URLtmp,row[3]);
		if(strstr(URLtmp,strURL)) 
		{
			//printf("The same\n");
       			strcat(packets,"InfoOK?");
			strcat(packets,row[0]);
			strcat(packets,"?");
			strcat(packets,row[1]);
			strcat(packets,"?");
			strcat(packets,row[2]);
			strcat(packets,"?");
			strcat(packets,row[3]);
			break;
		}	
        
        }
	
	strcat(packets,"?");
   } 


   strcat(packets,"$");//$identify the end 
   if(res) {
   	mysql_free_result(res);
   }
   mysql_close(conn);
   
}
