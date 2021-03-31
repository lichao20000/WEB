create table tab_terminal_version (
   version                  varchar2(100)                     not null,   	
   ver_type               		NUMBER(1)                     not null,     	
   down_path         		varchar2(255)                    not null,			
   updatetime                NUMBER(13)                    not null   			
);
insert into tab_terminal_version(ver_type,version,down_path,updatetime)values(1,'0','0',0);
insert into tab_terminal_version(ver_type,version,down_path,updatetime)values(2,'0','0',0);
create table TAB_TERMVER_EDITLOG (
   ver_id                    NUMBER(13)                 primary  key  not null,  		
   version                  varchar2(100)                     not null,   	
   ver_type               		NUMBER(1)                     not null,     	
   down_path         		varchar2(255)                    not null,			
   updator                 varchar2(100)         ,
   updatetime                NUMBER(13)                    not null   			
);
select b.*,sortId from (select a.*,rownum as sortId from 
  (select VER_ID,VERSION,VER_TYPE,DOWN_PATH,UPDATOR,UPDATETIME from  TAB_TERMVER_EDITLOG  where VER_TYPE=1 order by updatetime desc ) a 
 where  rownum<=19) b where sortId>10;