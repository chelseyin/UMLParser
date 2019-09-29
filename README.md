# CSSE374 Project

## Author
Kaiyu Xie @xiek  
Chen Yin @yinc

# Command

Run Configurations on src/main/Main.java to use these commands

| args | param | Description |
| :--- | :--- | :---: |
| -class | class name | parse specific class |
| -dir | directory path | parse whole directory |
| -recursive | N/A | recrusively parse |
| -Public | N/A | render Public Class/Variable/Method |
| -Protected | N/A | render Public and Protected Class/Variable/Method |
| -Private | N/A | render Public, Protected, and Private Class/Variable/Method |

# Milstone 1

## Example of Command
```
-class java.lang.String

-dir C:\Users\yinc\Desktop\CSSE374\Project\csse374-project-master\src\test

-class javax.swing.JComponent -recursive

-class javax.swing.JComponent -Public

-class javax.swing.JComponent -Protected

-class javax.swing.JComponent -Private
```

## Contribution

### Kaiyu Xie
Design UML  
Implement Parser, ParserM1  
Implement Main, MyClassReader  

### Chen Yin
Design UML  
Implement Drawer, DrawerM1  
Implement plantuml svg convention  

## UML Diagram
