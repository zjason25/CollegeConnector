def process_file(file_path):
  total_ts = 0
  total_tj = 0
  count = 0

  with open(file_path, 'r') as file:
    for line in file:
      nums = line.split(",")
      ts = int(nums[0][2:].strip())
      tj = int(nums[1][3:].strip())

      total_ts += ts
      total_tj += tj
      count += 1

  if count > 0:
    average_ts = total_ts / count
    average_tj = total_tj / count
    print("Average TS:", average_ts)
    print("Average TJ:", average_tj)
  else:
    print("No data found in the file.")

file_path = "\log.txt"  # Replace with the actual file path
process_file(file_path)
