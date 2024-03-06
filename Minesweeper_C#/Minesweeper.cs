using System.Diagnostics;

try
{
    // Get the path of the executing assembly
string assemblyPath = System.Reflection.Assembly.GetExecutingAssembly().Location;

// Get the directory of the executing assembly
string workingDirectory = Path.GetDirectoryName(assemblyPath);

// Get the parent directory until you reach the project root
while (Path.GetFileName(workingDirectory) != "Minesweeper_C#")
{
    workingDirectory = Directory.GetParent(workingDirectory).FullName;
}

string batchFilePath = Path.Combine(workingDirectory, "Minesweeper.bat");

    // Create a new process to run the batch file
    ProcessStartInfo psi = new("cmd.exe", "/c " + batchFilePath)
    {
        RedirectStandardOutput = true,
        UseShellExecute = false,
        CreateNoWindow = true
    };

    Process process = new()
    {
        StartInfo = psi
    };
    process.StartInfo.WorkingDirectory = workingDirectory;  
    process.Start();

    // Wait for the batch file to finish executing
    process.WaitForExit();
    Environment.Exit(0);
}
catch (Exception ex)
{
    Console.WriteLine("Error executing Minesweeper.bat: " + ex.Message);
}
