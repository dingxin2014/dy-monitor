USE [eReceipt]
GO

/****** Object:  Table [dbo].[T_ERECEIPT_REQUEST]    Script Date: 2016/10/27 13:49:06 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

SET ANSI_PADDING ON
GO

CREATE TABLE [dbo].[T_ERECEIPT_REQUEST](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[customNo] [char](32) NULL,
	[payBankAccount] [varchar](64) NULL,
	[payBankAccountName] [nvarchar](512) NULL,
	[flag] [tinyint] NULL,
	[failTimes] [tinyint] NULL,
	[fromDate] [datetime] NULL,
	[toDate] [datetime] NULL,
	[downloadFlag] [tinyint] NULL,
	[downloadFailTimes] [tinyint] NULL,
	[creator] [int] NULL,
	[createTime] [datetime] NULL,
	[updator] [int] NULL,
	[updateTime] [datetime] NULL,
	[receiptBankAccount] [varchar](64) NULL,
	[receiptBankAccountName] [nvarchar](512) NULL,
	[respCode] [varchar](64) NULL,
	[respMessage] [nvarchar](1024) NULL,
	[respFilename] [nvarchar](128) NULL,
	[bankCode] [varchar](16) NULL,
 CONSTRAINT [PK_T_ERECEIPT_REQUEST] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

SET ANSI_PADDING OFF
GO

ALTER TABLE [dbo].[T_ERECEIPT_REQUEST] ADD  DEFAULT ((0)) FOR [downloadFlag]
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'主键' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'T_ERECEIPT_REQUEST', @level2type=N'COLUMN',@level2name=N'id'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'发送报文流水号(uuid)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'T_ERECEIPT_REQUEST', @level2type=N'COLUMN',@level2name=N'customNo'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'发送报文付款账号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'T_ERECEIPT_REQUEST', @level2type=N'COLUMN',@level2name=N'payBankAccount'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'发送报文付款名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'T_ERECEIPT_REQUEST', @level2type=N'COLUMN',@level2name=N'payBankAccountName'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'1请求前置机成功
0请求前置机失败' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'T_ERECEIPT_REQUEST', @level2type=N'COLUMN',@level2name=N'flag'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'失败次数' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'T_ERECEIPT_REQUEST', @level2type=N'COLUMN',@level2name=N'failTimes'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'查询报文开始时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'T_ERECEIPT_REQUEST', @level2type=N'COLUMN',@level2name=N'fromDate'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'查询报文结束时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'T_ERECEIPT_REQUEST', @level2type=N'COLUMN',@level2name=N'toDate'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'1下载文件成功
0下载文件失败' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'T_ERECEIPT_REQUEST', @level2type=N'COLUMN',@level2name=N'downloadFlag'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'创建人' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'T_ERECEIPT_REQUEST', @level2type=N'COLUMN',@level2name=N'creator'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'创建时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'T_ERECEIPT_REQUEST', @level2type=N'COLUMN',@level2name=N'createtTime'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'修改人' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'T_ERECEIPT_REQUEST', @level2type=N'COLUMN',@level2name=N'updator'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'修改时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'T_ERECEIPT_REQUEST', @level2type=N'COLUMN',@level2name=N'updateTime'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'发送报文收款账号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'T_ERECEIPT_REQUEST', @level2type=N'COLUMN',@level2name=N'receiptBankAccount'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'发送报文收款名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'T_ERECEIPT_REQUEST', @level2type=N'COLUMN',@level2name=N'receiptBankAccountName'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'银行返回代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'T_ERECEIPT_REQUEST', @level2type=N'COLUMN',@level2name=N'respCode'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'银行返回message
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'T_ERECEIPT_REQUEST', @level2type=N'COLUMN',@level2name=N'respMessage'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'银行返回的文件名' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'T_ERECEIPT_REQUEST', @level2type=N'COLUMN',@level2name=N'respFilename'
GO


