# 简介  
这是一个用于湖南卫视（也称芒果台，马桶台）[第28届电视金鹰奖](http://www.mgtv.com/v/2016/jyj2016/)的自动投票器。  
写这个东西是受一位学姐强烈所托，而我单纯的认为这是个可以练手的小工具（然而实际上比我想的要复杂。  
这个学姐痴迷于一个叫王凯的帅哥（虽然我并不知道这是谁），所以在她的监督下，默认这个工具是投给这位同学的。  
这个投票器在运行后，会读取目录下“accounts.txt”的用户名和密码，自动填入浏览器，需要用户在运行界面内（而非浏览器中）手动输入验证码。  
accounts.txt的格式为每单独行：用户名@密码（回车符），最后一行需要一行无空格的空行，以下为示例：  
> 1234562222@qwe123
> 0987621454@gasddad
>

#简要原理  
这是一个Netbeen J2SE工程，无图形UI    
目标页面运用了大量Ajax技术，密码传输用了自己的一个脚本加密（并不能看懂ORZ    
因而模拟登录如果用传统的构造包方式会相对复杂，于是我偷懒用了自动化测试工具[selenium](http://www.seleniumhq.org/)。    
即通过调用selenium的API嵌入一个浏览器，登录后拿到cookie。再抽取相应字段定时以GET方式访问投票php。  
访问投票php需要以下五个参数：  
+ sid （奖项）  
+ star_id (明星)  
+ client （访问端）  
+ uuid (账户)  
+ ticket (不明，可能类似校验)  
前三个是相对静态的参数，后两个登录后可得，cookie的有效时间充分长，因而我们可以在注销后继续投票。  

#注意事项
1. Windows下需要指定Scanner的编码，因为通常中文版的系统编码是GBK,但是netbean的默认编码是utf-8;
2. 默认driver在Windows下是Edge, 在macOS下是Firefox,需要按照相应浏览器（建议非Windows 10用户尝试IE driver, 换用不同  
浏览器需到[selenium](http://www.seleniumhq.org/)下载相应driver并以默认路径安装该浏览器）
    *经测试，Firefox for Windows driver目前处于严重bug状态*