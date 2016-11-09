USE [eReceipt]
GO

/****** Object:  Table [dbo].[T_ERECEIPT_LOG]    Script Date: 2016/10/27 15:53:41 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[T_ERECEIPT_LOG](
	[id] [bigint]  IDENTITY(1,1) NOT NULL,
	[operate] [nvarchar](64) NULL,
	[result] [nvarchar](32) NULL,
	[remark] [nvarchar](2048) NULL,
	[operator] [int] NULL,
	[operateTime] [datetime] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'主键' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'T_ERECEIPT_LOG', @level2type=N'COLUMN',@level2name=N'id'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'操作' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'T_ERECEIPT_LOG', @level2type=N'COLUMN',@level2name=N'operate'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'备注' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'T_ERECEIPT_LOG', @level2type=N'COLUMN',@level2name=N'remark'
GO


