if exists (select 1
            from  sysobjects
           where  id = object_id('T_ERECEIPT_UPLOAD')
            and   type = 'U')
   drop table T_ERECEIPT_UPLOAD
go

/*==============================================================*/
/* Table: T_ERECEIPT_UPLOAD                                     */
/*==============================================================*/
create table T_ERECEIPT_UPLOAD (
   id                   int identity(1,1)    not null,
   billId               int                  null,
   requestId            int                  null,
   fileName             varchar(256)         null,
   virtualPath          varchar(256)         null,
   uploadFlag           tinyint              null,
   creator              int                  null,
   createTime           datetime             null,
   updator              int                  null,
   updateTime           datetime             null,
   constraint PK_T_ERECEIPT_UPLOAD primary key (id)
)
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('T_ERECEIPT_UPLOAD')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'id')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = 'dbo' -- user_name()
execute sp_dropextendedproperty 'MS_Description', 
   'user', @CurrentUser, 'table', 'T_ERECEIPT_UPLOAD', 'column', 'id'

end


select @CurrentUser = 'dbo' --user_name()
execute sp_addextendedproperty 'MS_Description', 
   'id',
   'user', @CurrentUser, 'table', 'T_ERECEIPT_UPLOAD', 'column', 'id'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('T_ERECEIPT_UPLOAD')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'billId')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = 'dbo' -- user_name()
execute sp_dropextendedproperty 'MS_Description', 
   'user', @CurrentUser, 'table', 'T_ERECEIPT_UPLOAD', 'column', 'billId'

end


select @CurrentUser = 'dbo' --user_name()
execute sp_addextendedproperty 'MS_Description', 
   '单据id',
   'user', @CurrentUser, 'table', 'T_ERECEIPT_UPLOAD', 'column', 'billId'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('T_ERECEIPT_UPLOAD')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'requestId')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = 'dbo' -- user_name()
execute sp_dropextendedproperty 'MS_Description', 
   'user', @CurrentUser, 'table', 'T_ERECEIPT_UPLOAD', 'column', 'requestId'

end


select @CurrentUser = 'dbo' --user_name()
execute sp_addextendedproperty 'MS_Description', 
   '请求id',
   'user', @CurrentUser, 'table', 'T_ERECEIPT_UPLOAD', 'column', 'requestId'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('T_ERECEIPT_UPLOAD')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'fileName')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = 'dbo' -- user_name()
execute sp_dropextendedproperty 'MS_Description', 
   'user', @CurrentUser, 'table', 'T_ERECEIPT_UPLOAD', 'column', 'fileName'

end


select @CurrentUser = 'dbo' --user_name()
execute sp_addextendedproperty 'MS_Description', 
   '文件名',
   'user', @CurrentUser, 'table', 'T_ERECEIPT_UPLOAD', 'column', 'fileName'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('T_ERECEIPT_UPLOAD')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'virtualPath')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = 'dbo' -- user_name()
execute sp_dropextendedproperty 'MS_Description', 
   'user', @CurrentUser, 'table', 'T_ERECEIPT_UPLOAD', 'column', 'virtualPath'

end


select @CurrentUser = 'dbo' --user_name()
execute sp_addextendedproperty 'MS_Description', 
   '虚拟路径',
   'user', @CurrentUser, 'table', 'T_ERECEIPT_UPLOAD', 'column', 'virtualPath'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('T_ERECEIPT_UPLOAD')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'uploadFlag')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = 'dbo' -- user_name()
execute sp_dropextendedproperty 'MS_Description', 
   'user', @CurrentUser, 'table', 'T_ERECEIPT_UPLOAD', 'column', 'uploadFlag'

end


select @CurrentUser = 'dbo' --user_name()
execute sp_addextendedproperty 'MS_Description', 
   '上传标志',
   'user', @CurrentUser, 'table', 'T_ERECEIPT_UPLOAD', 'column', 'uploadFlag'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('T_ERECEIPT_UPLOAD')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'creator')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = 'dbo' -- user_name()
execute sp_dropextendedproperty 'MS_Description', 
   'user', @CurrentUser, 'table', 'T_ERECEIPT_UPLOAD', 'column', 'creator'

end


select @CurrentUser = 'dbo' --user_name()
execute sp_addextendedproperty 'MS_Description', 
   '创建人',
   'user', @CurrentUser, 'table', 'T_ERECEIPT_UPLOAD', 'column', 'creator'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('T_ERECEIPT_UPLOAD')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'createTime')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = 'dbo' -- user_name()
execute sp_dropextendedproperty 'MS_Description', 
   'user', @CurrentUser, 'table', 'T_ERECEIPT_UPLOAD', 'column', 'createTime'

end


select @CurrentUser = 'dbo' --user_name()
execute sp_addextendedproperty 'MS_Description', 
   '创建时间',
   'user', @CurrentUser, 'table', 'T_ERECEIPT_UPLOAD', 'column', 'createTime'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('T_ERECEIPT_UPLOAD')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'updator')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = 'dbo' -- user_name()
execute sp_dropextendedproperty 'MS_Description', 
   'user', @CurrentUser, 'table', 'T_ERECEIPT_UPLOAD', 'column', 'updator'

end


select @CurrentUser = 'dbo' --user_name()
execute sp_addextendedproperty 'MS_Description', 
   '更新人',
   'user', @CurrentUser, 'table', 'T_ERECEIPT_UPLOAD', 'column', 'updator'
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('T_ERECEIPT_UPLOAD')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'updateTime')
)
begin
   declare @CurrentUser sysname
select @CurrentUser = 'dbo' -- user_name()
execute sp_dropextendedproperty 'MS_Description', 
   'user', @CurrentUser, 'table', 'T_ERECEIPT_UPLOAD', 'column', 'updateTime'

end


select @CurrentUser = 'dbo' --user_name()
execute sp_addextendedproperty 'MS_Description', 
   '更新时间',
   'user', @CurrentUser, 'table', 'T_ERECEIPT_UPLOAD', 'column', 'updateTime'
go
