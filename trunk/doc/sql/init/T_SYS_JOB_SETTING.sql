USE [eReceipt]
GO

/****** Object:  Table [dbo].[T_SYS_JOB_SETTING]    Script Date: 2016/10/29 18:03:41 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

SET ANSI_PADDING ON
GO


CREATE TABLE [dbo].[T_SYS_JOB_SETTING](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[appCode] [varchar](32) NULL,
	[appName] [nvarchar](512) NULL,
	[ip] [varchar](1024) NULL,
	[method] [varchar] (128) NULL,
	[cron] [varchar](32) NULL,
	[remark] [nvarchar](1024) NULL,
	[suspendFlag] [tinyint] NULL DEFAULT 0,
	[deleteFlag] [tinyint] NULL DEFAULT 0,
	[status] [int] NULL DEFAULT 0,
	[creator] [int] NULL,
	[createTime] [datetime] NULL,
	[updator] [int] NULL,
	[updateTime] [datetime] NULL,
 CONSTRAINT [PK__T_ERECEIPT_JOB_SETTING] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

SET ANSI_PADDING OFF
GO


