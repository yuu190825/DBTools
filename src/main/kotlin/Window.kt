import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.*

class Window {

    // Init Value
    private val window = JFrame()
    private val panel = JPanel()

    // Set Value
    var fromDbType: Byte = 3
    var toDbType: Byte = 3
    var func: Byte = 1
    var mode: Byte = 1
    val tabNameList = mutableSetOf<String>()

    // Set Value (JTextField)
    val fromDbUrl = JTextField()
    val fromDbName = JTextField()
    val fromDbUser = JTextField()
    val fromDbPass = JPasswordField()
    val toDbUrl = JTextField()
    val toDbName = JTextField()
    val toDbUser = JTextField()
    val toDbPass = JPasswordField()
    val record = JTextField()
    val tabName = JTextField()
    val total = JTextField()

    // Components
    private val rdBtnFromDbTypeOne = JRadioButton("Oracle DB")
    private val rdBtnFromDbTypeTwo = JRadioButton("SQL Server")
    private val rdBtnFromDbTypeThree = JRadioButton("MySQL")
    private val rdBtnToDbTypeOne = JRadioButton("Oracle DB")
    private val rdBtnToDbTypeTwo = JRadioButton("SQL Server")
    private val rdBtnToDbTypeThree = JRadioButton("MySQL")
    private val rdBtnFuncOne = JRadioButton("Transfer")
    private val rdBtnFuncTwo = JRadioButton("Compare")
    private val rdBtnModeOne = JRadioButton("To File")
    private val rdBtnModeTwo = JRadioButton("To DB")
    private val btnTabNameAdd = JButton("Add")
    private val btnTabNameRemove = JButton("Remove")
    val btnStart = JButton("Start")

    fun print() {

        // Set Window
        window.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        window.title = "DBTools"
        window.setSize(600, 550)
        // End

        // Set Panel
        panel.layout = null
        window.contentPane = panel
        // End

        // Set Block 1 (To DB Setter)
        val lblFromDbType = JLabel("From DB Type:")
        lblFromDbType.setBounds(5, 0, 295, 25)
        panel.add(lblFromDbType)

        rdBtnFromDbTypeOne.setBounds(0, 25, 100, 25)
        rdBtnFromDbTypeOne.actionCommand = "Oracle DB"
        rdBtnFromDbTypeOne.addActionListener(FromDBTypeChange())
        panel.add(rdBtnFromDbTypeOne)

        rdBtnFromDbTypeTwo.setBounds(100, 25, 100, 25)
        rdBtnFromDbTypeTwo.actionCommand = "SQL Server"
        rdBtnFromDbTypeTwo.addActionListener(FromDBTypeChange())
        panel.add(rdBtnFromDbTypeTwo)

        rdBtnFromDbTypeThree.setBounds(200, 25, 100, 25)
        rdBtnFromDbTypeThree.isSelected = true
        rdBtnFromDbTypeThree.actionCommand = "MySQL"
        rdBtnFromDbTypeThree.addActionListener(FromDBTypeChange())
        panel.add(rdBtnFromDbTypeThree)

        val lblFromDbUrl = JLabel("From DB URL:")
        lblFromDbUrl.setBounds(5, 50, 295, 25)
        panel.add(lblFromDbUrl)

        fromDbUrl.setBounds(0, 75, 300, 25)
        panel.add(fromDbUrl)

        val lblFromDbName = JLabel("From DB Name:")
        lblFromDbName.setBounds(5, 100, 295, 25)
        panel.add(lblFromDbName)

        fromDbName.setBounds(0, 125, 300, 25)
        panel.add(fromDbName)

        val lblFromDbUser = JLabel("From DB Username:")
        lblFromDbUser.setBounds(5, 150, 295, 25)
        panel.add(lblFromDbUser)

        fromDbUser.setBounds(0, 175, 300, 25)
        panel.add(fromDbUser)

        val lblFromDbPass = JLabel("From DB Password:")
        lblFromDbPass.setBounds(5, 200, 295, 25)
        panel.add(lblFromDbPass)

        fromDbPass.setBounds(0, 225, 300, 25)
        panel.add(fromDbPass)
        // End

        // Set Block 2 (To DB Setter)
        val lblToDbType = JLabel("To DB Type:")
        lblToDbType.setBounds(5, 250, 295, 25)
        panel.add(lblToDbType)

        rdBtnToDbTypeOne.setBounds(0, 275, 100, 25)
        rdBtnToDbTypeOne.actionCommand = "Oracle DB"
        rdBtnToDbTypeOne.addActionListener(ToDBTypeChange())
        panel.add(rdBtnToDbTypeOne)

        rdBtnToDbTypeTwo.setBounds(100, 275, 100, 25)
        rdBtnToDbTypeTwo.actionCommand = "SQL Server"
        rdBtnToDbTypeTwo.addActionListener(ToDBTypeChange())
        panel.add(rdBtnToDbTypeTwo)

        rdBtnToDbTypeThree.setBounds(200, 275, 100, 25)
        rdBtnToDbTypeThree.isSelected = true
        rdBtnToDbTypeThree.actionCommand = "MySQL"
        rdBtnToDbTypeThree.addActionListener(ToDBTypeChange())
        panel.add(rdBtnToDbTypeThree)

        val lblToDbUrl = JLabel("To DB URL:")
        lblToDbUrl.setBounds(5, 300, 295, 25)
        panel.add(lblToDbUrl)

        toDbUrl.setBounds(0, 325, 300, 25)
        panel.add(toDbUrl)

        val lblToDbName = JLabel("To DB Name:")
        lblToDbName.setBounds(5, 350, 295, 25)
        panel.add(lblToDbName)

        toDbName.setBounds(0, 375, 300, 25)
        panel.add(toDbName)

        val lblToDbUser = JLabel("To DB Username:")
        lblToDbUser.setBounds(5, 400, 295, 25)
        panel.add(lblToDbUser)

        toDbUser.setBounds(0, 425, 300, 25)
        panel.add(toDbUser)

        val lblToDbPass = JLabel("To DB Password:")
        lblToDbPass.setBounds(5, 450, 295, 25)
        panel.add(lblToDbPass)

        toDbPass.setBounds(0, 475, 300, 25)
        panel.add(toDbPass)
        // End

        // Set Block 3 (Function Setter & Start Button)
        val lblFunc = JLabel("Function Mode:")
        lblFunc.setBounds(305, 0, 295, 25)
        panel.add(lblFunc)

        rdBtnFuncOne.setBounds(300, 25, 150, 25)
        rdBtnFuncOne.isSelected = true
        rdBtnFuncOne.actionCommand = "Transfer"
        rdBtnFuncOne.addActionListener(FunctionModeChange())
        panel.add(rdBtnFuncOne)

        rdBtnFuncTwo.setBounds(450, 25, 150, 25)
        rdBtnFuncTwo.actionCommand = "Compare"
        rdBtnFuncTwo.addActionListener(FunctionModeChange())
        panel.add(rdBtnFuncTwo)

        val lblMode = JLabel("Transfer Mode:")
        lblMode.setBounds(305, 50, 295, 25)
        panel.add(lblMode)

        rdBtnModeOne.setBounds(300, 75, 150, 25)
        rdBtnModeOne.isSelected = true
        rdBtnModeOne.actionCommand = "To File"
        rdBtnModeOne.addActionListener(TransferModeChange())
        panel.add(rdBtnModeOne)

        rdBtnModeTwo.setBounds(450, 75, 150, 25)
        rdBtnModeTwo.actionCommand = "To DB"
        rdBtnModeTwo.addActionListener(TransferModeChange())
        panel.add(rdBtnModeTwo)

        val lblRecord = JLabel("Record:")
        lblRecord.setBounds(305, 100, 295, 25)
        panel.add(lblRecord)

        record.setBounds(300, 125, 300, 25)
        panel.add(record)

        val lblTabName = JLabel("Table Name:")
        lblTabName.setBounds(305, 150, 295, 25)
        panel.add(lblTabName)

        tabName.setBounds(300, 175, 300, 25)
        panel.add(tabName)

        btnTabNameAdd.setBounds(300, 200, 150, 25)
        btnTabNameAdd.actionCommand = "Add"
        btnTabNameAdd.addActionListener(TabNameListControl())
        panel.add(btnTabNameAdd)

        btnTabNameRemove.setBounds(450, 200, 150, 25)
        btnTabNameRemove.actionCommand = "Remove"
        btnTabNameRemove.addActionListener(TabNameListControl())
        panel.add(btnTabNameRemove)

        val lblTotalRow = JLabel("Total Row:")
        lblTotalRow.setBounds(305, 225, 295, 25)
        panel.add(lblTotalRow)

        total.setBounds(300, 250, 300, 25)
        panel.add(total)

        btnStart.setBounds(300, 475, 300, 25)
        panel.add(btnStart)
        // End

        window.isVisible = true
    }

    fun start() {
        rdBtnFromDbTypeOne.isEnabled = false
        rdBtnFromDbTypeTwo.isEnabled = false
        rdBtnFromDbTypeThree.isEnabled = false
        rdBtnToDbTypeOne.isEnabled = false
        rdBtnToDbTypeTwo.isEnabled = false
        rdBtnToDbTypeThree.isEnabled = false
        rdBtnFuncOne.isEnabled = false
        rdBtnFuncTwo.isEnabled = false
        rdBtnModeOne.isEnabled = false
        rdBtnModeTwo.isEnabled = false
        btnTabNameAdd.isEnabled = false
        btnTabNameRemove.isEnabled = false
        btnStart.isEnabled = false
    }

    fun end() {
        rdBtnFromDbTypeOne.isEnabled = true
        rdBtnFromDbTypeTwo.isEnabled = true
        rdBtnFromDbTypeThree.isEnabled = true
        rdBtnToDbTypeOne.isEnabled = true
        rdBtnToDbTypeTwo.isEnabled = true
        rdBtnToDbTypeThree.isEnabled = true
        rdBtnFuncOne.isEnabled = true
        rdBtnFuncTwo.isEnabled = true
        rdBtnModeOne.isEnabled = true
        rdBtnModeTwo.isEnabled = true
        btnTabNameAdd.isEnabled = true
        btnTabNameRemove.isEnabled = true
        btnStart.isEnabled = true
    }

    private inner class FromDBTypeChange: ActionListener {
        override fun actionPerformed(e: ActionEvent?) {
            when (e?.actionCommand) {
                "Oracle DB" -> {
                    fromDbType = 1
                    rdBtnFromDbTypeTwo.isSelected = false
                    rdBtnFromDbTypeThree.isSelected = false
                }
                "SQL Server" -> {
                    fromDbType = 2
                    rdBtnFromDbTypeOne.isSelected = false
                    rdBtnFromDbTypeThree.isSelected = false
                }
                "MySQL" -> {
                    fromDbType = 3
                    rdBtnFromDbTypeOne.isSelected = false
                    rdBtnFromDbTypeTwo.isSelected = false
                }
            }
        }
    }

    private inner class ToDBTypeChange: ActionListener {
        override fun actionPerformed(e: ActionEvent?) {
            when (e?.actionCommand) {
                "Oracle DB" -> {
                    toDbType = 1
                    rdBtnToDbTypeTwo.isSelected = false
                    rdBtnToDbTypeThree.isSelected = false
                }
                "SQL Server" -> {
                    toDbType = 2
                    rdBtnToDbTypeOne.isSelected = false
                    rdBtnToDbTypeThree.isSelected = false
                }
                "MySQL" -> {
                    toDbType = 3
                    rdBtnToDbTypeOne.isSelected = false
                    rdBtnToDbTypeTwo.isSelected = false
                }
            }
        }
    }

    private inner class FunctionModeChange: ActionListener {
        override fun actionPerformed(e: ActionEvent?) {
            when (e?.actionCommand) {
                "Transfer" -> {
                    func = 1
                    rdBtnFuncTwo.isSelected = false
                    rdBtnModeOne.isEnabled = true
                    rdBtnModeTwo.isEnabled = true
                }
                "Compare" -> {
                    func = 2
                    rdBtnFuncOne.isSelected = false
                    rdBtnModeOne.isEnabled = false
                    rdBtnModeTwo.isEnabled = false
                }
            }
        }
    }

    private inner class TransferModeChange: ActionListener {
        override fun actionPerformed(e: ActionEvent?) {
            when (e?.actionCommand) {
                "To File" -> {
                    mode = 1
                    rdBtnModeTwo.isSelected = false
                }
                "To DB" -> {
                    mode = 2
                    rdBtnModeOne.isSelected = false
                }
            }
        }
    }

    private inner class TabNameListControl: ActionListener {
        override fun actionPerformed(e: ActionEvent?) {
            when (e?.actionCommand) {
                "Add" -> tabNameList.add(tabName.text)
                "Remove" -> tabNameList.remove(tabName.text)
            }

            println("Table Name List: $tabNameList")
        }
    }
}