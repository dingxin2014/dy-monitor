USE [eReceipt]
GO

/****** Object:  Table [dbo].[T_ERECEIPT_BILL]    Script Date: 2016/10/27 13:47:08 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

SET ANSI_PADDING ON
GO

CREATE TABLE [dbo].[T_ERECEIPT_BILL](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[payInfoId] [varchar](64) NULL,
	[bankCode] [varchar](16) NULL,
	[amount] [numeric](18, 4) NULL,
	[easSerialNumber] [varchar](128) NULL,
	[payInfoCreateTime] [datetime] NULL,
	[explanation] [nvarchar](256) NULL,
	[payBankAccount] [varchar](64) NULL,
	[payBankAccountName] [nvarchar](256) NULL,
	[payBankName] [nvarchar](256) NULL,
	[receiptBankAccount] [varchar](64) NULL,
	[receiptBankAccountName] [nvarchar](256) NULL,
	[receiptBankName] [nvarchar](256) NULL,
	[payFinishTime] [datetime] NULL,
	[bankStatus] [nvarchar](128) NULL,
	[bankMessage] [nvarchar](256) NULL,
	[serialNumber] [varchar](64) NULL,
	[easBillNumber] [varchar](64) NULL,
	[billNumber] [nvarchar](128) NULL,
	[flag] [int] NULL DEFAULT ((0)),
	[requestId] [int] NULL,
	[creator] [int] NULL,
	[createTime] [datetime] NULL,
	[updator] [int] NULL,
	[updateTime] [datetime] NULL,
	[deleteFlag] [tinyint] DEFAULT 0,
 CONSTRAINT [PK_T_ERECEIPT_BILL] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

SET ANSI_PADDING OFF
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'id ' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'T_ERECEIPT_BILL', @level2type=N'COLUMN',@level2name=N'id'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'T_EBG_PaymentInfo主键' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'T_ERECEIPT_BILL', @level2type=N'COLUMN',@level2name=N'payInfoId'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'银行类型 (T_EBG_PaymentInfo的bank_Version_Id)
银行类型(T_EBG_PaymentInfo的bank_Version_Id)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'T_ERECEIPT_BILL', @level2type=N'COLUMN',@level2name=N'bankCode'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'付款金额(T_EBG_PaymentInfo的total_Amount)
付款金额(T_EBG_PaymentInfo的total_Amount)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'T_ERECEIPT_BILL', @level2type=N'COLUMN',@level2name=N'amount'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'easId(T_EBG_PaymentInfo的detail_seq_id)
easID(T_EBG_PaymentInfo的detail_seq_id)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'T_ERECEIPT_BILL', @level2type=N'COLUMN',@level2name=N'easSerialNumber'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'付款时间（银企对接银行时间)(T_EBG_PaymentInfo的insert_Time)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'T_ERECEIPT_BILL', @level2type=N'COLUMN',@level2name=N'payInfoCreateTime'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'付款摘要(T_EBG_PaymentInfo的explanation)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'T_ERECEIPT_BILL', @level2type=N'COLUMN',@level2name=N'explanation'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'付款帐号(T_EBG_PaymentInfo的acc_No)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'T_ERECEIPT_BILL', @level2type=N'COLUMN',@level2name=N'payBankAccount'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'付款人名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'T_ERECEIPT_BILL', @level2type=N'COLUMN',@level2name=N'payBankAccountName'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'付款银行' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'T_ERECEIPT_BILL', @level2type=N'COLUMN',@level2name=N'payBankName'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'付款人账号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'T_ERECEIPT_BILL', @level2type=N'COLUMN',@level2name=N'receiptBankAccount'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'收款账户名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'T_ERECEIPT_BILL', @level2type=N'COLUMN',@level2name=N'receiptBankAccountName'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'收款银行名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'T_ERECEIPT_BILL', @level2type=N'COLUMN',@level2name=N'receiptBankName'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'付款时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'T_ERECEIPT_BILL', @level2type=N'COLUMN',@level2name=N'payFinishTime'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'付款状态' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'T_ERECEIPT_BILL', @level2type=N'COLUMN',@level2name=N'bankStatus'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'银行返回的付款信息(T_EBG_PaymentInfo的bank_msg)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'T_ERECEIPT_BILL', @level2type=N'COLUMN',@level2name=N'bankMessage'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'业务编号
(T_EBG_PaymentInfo的bank_Detail_Seq_Id)(与pdf内业务编号一致)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'T_ERECEIPT_BILL', @level2type=N'COLUMN',@level2name=N'serialNumber'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'金蝶付款单ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'T_ERECEIPT_BILL', @level2type=N'COLUMN',@level2name=N'easBillNumber'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'业务系统单据号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'T_ERECEIPT_BILL', @level2type=N'COLUMN',@level2name=N'billNumber'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'1发送请求到前置机成功0未发送-1发送请求到前置机失败' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'T_ERECEIPT_BILL', @level2type=N'COLUMN',@level2name=N'flag'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'创建人' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'T_ERECEIPT_BILL', @level2type=N'COLUMN',@level2name=N'creator'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'创建时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'T_ERECEIPT_BILL', @level2type=N'COLUMN',@level2name=N'createTime'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'修改人' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'T_ERECEIPT_BILL', @level2type=N'COLUMN',@level2name=N'updator'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'修改时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'T_ERECEIPT_BILL', @level2type=N'COLUMN',@level2name=N'updateTime'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'删除标记 0未删除 1 删除' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'T_ERECEIPT_BILL', @level2type=N'COLUMN',@level2name=N'deleteFlag'
GO


