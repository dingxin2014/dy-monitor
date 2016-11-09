USE [eReceipt]
GO

/****** Object:  Table [dbo].[T_ERECEIPT_ACCESS_SETTING]    Script Date: 2016/10/28 15:17:21 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

SET ANSI_PADDING ON
GO

CREATE TABLE [dbo].[T_ERECEIPT_ACCESS_SETTING](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[ipAddress] [varchar](1024) NULL,
	[appCode] [varchar](32) NULL,
	[appName] [nvarchar](512) NULL,
	[remark] [nvarchar](1024) NULL,
	[deleteFlag] [tinyint] NULL,
	[status] [tinyint] NULL,
	[creator] [int] NULL,
	[createTime] [datetime] NULL,
	[updator] [int] NULL,
	[updateTime] [datetime] NULL,
 CONSTRAINT [PK__T_ ERECE__3213E83FA61CE169] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

SET ANSI_PADDING OFF
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'主键' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'T_ERECEIPT_ACCESS_SETTING', @level2type=N'COLUMN',@level2name=N'id'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'IP地址用，分隔' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'T_ERECEIPT_ACCESS_SETTING', @level2type=N'COLUMN',@level2name=N'ipAddress'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'业务appCode' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'T_ERECEIPT_ACCESS_SETTING', @level2type=N'COLUMN',@level2name=N'appCode'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'app名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'T_ERECEIPT_ACCESS_SETTING', @level2type=N'COLUMN',@level2name=N'appName'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'备注' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'T_ERECEIPT_ACCESS_SETTING', @level2type=N'COLUMN',@level2name=N'remark'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'删除标识 0未删除 1 删除' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'T_ERECEIPT_ACCESS_SETTING', @level2type=N'COLUMN',@level2name=N'deleteFlag'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'1启用 0 禁用' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'T_ERECEIPT_ACCESS_SETTING', @level2type=N'COLUMN',@level2name=N'status'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'创建人' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'T_ERECEIPT_ACCESS_SETTING', @level2type=N'COLUMN',@level2name=N'creator'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'创建时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'T_ERECEIPT_ACCESS_SETTING', @level2type=N'COLUMN',@level2name=N'createTime'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'修改人' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'T_ERECEIPT_ACCESS_SETTING', @level2type=N'COLUMN',@level2name=N'updator'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'修改时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'T_ERECEIPT_ACCESS_SETTING', @level2type=N'COLUMN',@level2name=N'updateTime'
GO


