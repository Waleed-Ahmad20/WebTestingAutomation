import os
import tkinter as tk
from tkinter import filedialog, messagebox, scrolledtext
from huggingface_hub import InferenceClient

HUGGING_FACE_API_KEY = "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"

client = InferenceClient(api_key=HUGGING_FACE_API_KEY)

def read_file(file_path):
    """Reads the content of a file."""
    try:
        with open(file_path, 'r') as file:
            return file.read()
    except FileNotFoundError:
        messagebox.showerror("Error", f"File not found: {file_path}")
        return None

def generate_test_cases(file_content, language="Java"):
    """Generates test cases for the given file content using Hugging Face InferenceClient."""
    messages = [
        {
            "role": "user",
            "content": f"""
            Analyze the following {language} code and generate detailed unit test cases for all functions in it. Use JUnit 5 for the test cases. Only give the code.

            {file_content}
            """
        }
    ]

    try:
        # Request to Hugging Face API using the new InferenceClient
        completion = client.chat.completions.create(
            model="Qwen/Qwen2.5-Coder-32B-Instruct", 
            messages=messages, 
            max_tokens=500
        )
        
        # Extracting the generated text from the response
        generated_text = completion.choices[0].message['content']
        return generated_text

    except Exception as e:
        messagebox.showerror("Error", f"Error communicating with Hugging Face API: {e}")
        return None

def write_test_file(test_cases, original_file_path):
    """Writes the generated test cases to a new file."""
    if test_cases:
        base, ext = os.path.splitext(original_file_path)
        test_file_path = f"{base}_test{ext}"
        with open(test_file_path, 'w') as file:
            file.write(test_cases)
        messagebox.showinfo("Success", f"Test cases written to {test_file_path}")
        return test_file_path
    else:
        messagebox.showwarning("Warning", "No test cases generated.")
        return None

def browse_file():
    """Handles file browsing to select a Java file."""
    file_path = filedialog.askopenfilename(title="Select a Java file", filetypes=[("Java files", "*.java")])
    if not file_path:
        return
    
    file_content = read_file(file_path)
    if file_content:
        language = "Java"
        test_cases = generate_test_cases(file_content, language)
        test_file_path = write_test_file(test_cases, file_path)
        if test_file_path:
            display_generated_test_cases(test_file_path)

def display_generated_test_cases(test_file_path):
    """Reads and displays the contents of the generated test file."""
    try:
        with open(test_file_path, 'r') as file:
            content = file.read()
            text_area.delete(1.0, tk.END)  # Clear existing content
            text_area.insert(tk.END, content)  # Insert the new content
    except Exception as e:
        messagebox.showerror("Error", f"Could not read the generated file: {e}")

def create_gui():
    """Creates the main GUI."""
    global text_area

    root = tk.Tk()
    root.title("AI Test Generation")
    root.geometry("600x400")
    root.config(bg="#f0f4f8")

    # Frame for the content
    main_frame = tk.Frame(root, bg="#f0f4f8")
    main_frame.pack(pady=20, padx=20, fill=tk.BOTH, expand=True)

    # Title label
    title_label = tk.Label(main_frame, text="AI Test Generation", font=("Arial", 24, "bold"), fg="#2f4f4f", bg="#f0f4f8")
    title_label.pack(pady=10)

    # Instruction label
    instruction_label = tk.Label(main_frame, text="Select a Java file to generate JUnit 5 test cases", font=("Arial", 14), fg="#555555", bg="#f0f4f8")
    instruction_label.pack(pady=10)

    # Browse button
    browse_button = tk.Button(main_frame, text="Browse File", command=browse_file, font=("Arial", 12), fg="white", bg="#4CAF50", relief="solid", width=20)
    browse_button.pack(pady=10)

    # Scrolled text area to display generated test cases
    text_area_frame = tk.Frame(main_frame, bg="#f0f4f8")
    text_area_frame.pack(pady=10, fill=tk.BOTH, expand=True)

    text_area = scrolledtext.ScrolledText(text_area_frame, wrap=tk.WORD, height=10, font=("Courier", 12), bg="#ffffff", fg="#333333", bd=2, relief="solid")
    text_area.pack(fill=tk.BOTH, expand=True)

    # Run the GUI
    root.mainloop()

if __name__ == "__main__":
    create_gui()
